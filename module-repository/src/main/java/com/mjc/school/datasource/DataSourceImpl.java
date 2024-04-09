package com.mjc.school.datasource;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjc.school.exception.DataFileNotFoundException;
import com.mjc.school.exception.DataFileReadException;
import com.mjc.school.exception.EntityIsNotCloneableException;
import com.mjc.school.model.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataSourceImpl<T extends Entity> implements DataSource<T> {
    private final Class<T> entityClass;
    private final Map<Long, T> values = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private long nextId = 1;

    protected DataSourceImpl(String dataFileName, Class<T> entityClass) {
        this.entityClass = entityClass;
        List<T> items = readDataFromFile(dataFileName, entityClass);
        indexItems(items);
    }

    private void indexItems(List<T> items) {
        for (T item: items) {
            this.values.put(item.getId(), item);
            if (item.getId() >= nextId) {
                nextId = item.getId() + 1;
            }
        }
    }

    private List<T> readDataFromFile(String dataFileName, Class<T> entityClass)  {
        ClassLoader classLoader = AuthorDataSource.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(dataFileName)) {
            if (is == null) {
                throw new DataFileNotFoundException(dataFileName);
            }

            ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
            JavaType mapperType = mapper.getTypeFactory().constructCollectionType(List.class, entityClass);
            return mapper.readValue(is, mapperType);
        } catch (IOException e) {
            throw new DataFileReadException(dataFileName, e);
        }
    }

    @Override
    public T findById(Long id) {
        lock.readLock().lock();
        try {
            T entity = this.values.get(id);
            return entity == null ? null : cloneEntity(entity);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<T> findAll() {
        lock.readLock().lock();
        try {
            return this.values.values().stream()
                            .map(this::cloneEntity)
                            .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public T save(T value) {
        T entityToSave = cloneEntity(value);

        if (entityToSave.getId() == 0) {
            entityToSave.setId(getNextId());
        } else if (entityToSave.getId() >= this.nextId) {
            this.nextId = entityToSave.getId() + 1;
        }

        lock.writeLock().lock();
        try {
            this.values.put(entityToSave.getId(), entityToSave);
        } finally {
            lock.writeLock().unlock();
        }

        return entityToSave;
    }

    @Override
    public T remove(Long id) {
        lock.writeLock().lock();
        try {
            return this.values.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public long count() {
        lock.readLock().lock();
        try {
            return this.values.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    private synchronized Long getNextId() {
        return this.nextId++;
    }

    private T cloneEntity(T entity) {
        try {
            return (T) entity.clone();
        } catch (CloneNotSupportedException e) {
            throw new EntityIsNotCloneableException(this.entityClass.getCanonicalName());
        }
    }
}
