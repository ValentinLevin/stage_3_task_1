package com.mjc.school.repository;

import java.util.List;

public interface Repository <T> {
    T findById(Long id);
    T save(T entity);
    boolean delete(T entity);
    boolean deleteById(Long id);
    List<T> findAll();
    boolean existsById(Long id);
    long count();
}
