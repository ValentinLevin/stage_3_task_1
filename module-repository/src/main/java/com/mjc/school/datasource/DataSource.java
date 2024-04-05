package com.mjc.school.datasource;

import com.mjc.school.model.Model;

public interface DataSource <T extends Model> {
    T findById(Long id);
    void save(T value);
    void remove(Long id);
    long count();
}
