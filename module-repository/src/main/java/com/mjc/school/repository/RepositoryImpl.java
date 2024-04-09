package com.mjc.school.repository;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.model.Entity;

import java.util.List;

public class RepositoryImpl<T extends Entity> implements Repository<T>{
    protected final DataSource<T> dataSource;

    protected RepositoryImpl(DataSource<T> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public T findById(Long id) {
        return this.dataSource.findById(id);
    }

    @Override
    public T save(T entity) {
        return this.dataSource.save(entity);
    }

    @Override
    public void delete(T entity) {
        this.deleteById(entity.getId());
    }

    @Override
    public void deleteById(Long id) {
        this.dataSource.remove(id);
    }

    @Override
    public List<T> findAll() {
        return this.dataSource.findAll();
    }
}
