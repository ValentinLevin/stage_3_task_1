package com.mjc.school.datasource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjc.school.exception.DataFileNotFoundException;
import com.mjc.school.exception.DataFileReadException;
import com.mjc.school.model.Author;
import com.mjc.school.model.Model;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataSourceImpl<T extends Model> implements DataSource<T> {
    private final Map<Long, T> values = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private long nextId = 1;

    protected DataSourceImpl(String dataFileName, Class<T> modelClass) {
        List<T> items = readDataFromFile(dataFileName, modelClass);
        indexData(items);
    }

    private void indexData(List<T> items) {
        for (T item: items) {
            this.values.put(item.getId(), item);
            if (item.getId() >= nextId) {
                nextId = item.getId() + 1;
            }
        }
    }

    private List<T> readDataFromFile(String dataFileName, Class<T> modelClass)  {
        ClassLoader classLoader = AuthorDataSource.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(dataFileName)) {
            if (is == null) {
                throw new DataFileNotFoundException(dataFileName);
            }

            ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
            JavaType mapperType = mapper.getTypeFactory().constructCollectionType(List.class, modelClass);
            return mapper.readValue(is, mapperType);
        } catch (IOException e) {
            throw new DataFileReadException(dataFileName, e);
        }
    }

    @Override
    public T findById(Long id) {
        lock.readLock().lock();
        try {
            return this.values.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(T value) {
        if (value.getId() == 0) {
            value.setId(getNextId());
        }

        lock.writeLock().lock();
        try {
            this.values.put(value.getId(), value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(Long id) {
        lock.writeLock().lock();
        try {
            this.values.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public long count() {
        return this.values.size();
    }

    private synchronized Long getNextId() {
        return this.nextId++;
    }
}
