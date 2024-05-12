package com.mjc.school.datasource;

import com.mjc.school.model.Entity;

import java.util.List;

public interface DataSource <T extends Entity> {
    T findById(Long id);
    List<T> findAll();
    List<T> findAll(long offset, long limit);
    T save(T value);
    boolean delete(Long id);
    long count();
    boolean existsById(Long id);
}
