package com.mjc.school.repository;

import java.util.List;

public interface Repository <T> {
    T findById(Long id);
    T save(T entity);
    boolean delete(T entity);
    boolean deleteById(Long id);
    List<T> findAll();

    /**
     * @param offset number of elements to skip. If the value is zero, the elements will be taken from the first element
     * @param limit number of elements no more than the method should return. If the value of "limit" parameter is -1, all the elements of the dataset will be returned
     * @return list of dataset elements
     */
    List<T> findAll(long offset, long limit);

    boolean existsById(Long id);
    long count();
}
