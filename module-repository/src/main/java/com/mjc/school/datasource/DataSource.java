package com.mjc.school.datasource;

import com.mjc.school.model.Model;

import java.util.List;

public interface DataSource <T extends Model> {
    T findById(Long id);
    List<T> findAll();
    T save(T value);
    T remove(Long id);
    long count();
}
