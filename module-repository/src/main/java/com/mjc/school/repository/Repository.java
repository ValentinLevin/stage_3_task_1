package com.mjc.school.repository;

import java.util.List;

public interface Repository <T> {
    T findById(Long id);
    T save(T entity);
    void delete(T entity);
    void deleteById(Long id);
    List<T> findAll();
}
