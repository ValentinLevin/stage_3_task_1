package com.mjc.school.repository;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.exception.EntityNullReferenceException;
import com.mjc.school.exception.EntityValidationException;
import com.mjc.school.exception.KeyNullReferenceException;
import com.mjc.school.model.Entity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class RepositoryImpl<T extends Entity> implements Repository<T>{
    protected final DataSource<T> dataSource;
    protected static final Validator validator;

    static {
        try(ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    protected RepositoryImpl(DataSource<T> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public T findById(Long id) {
        if (id == null) {
            throw new KeyNullReferenceException();
        }
        return this.dataSource.findById(id);
    }

    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new EntityNullReferenceException();
        }

        validateEntity(entity);

        return this.dataSource.save(entity);
    }

    @Override
    public boolean delete(T entity) {
        if (entity == null) {
            throw new EntityNullReferenceException();
        }
        return this.deleteById(entity.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            throw new KeyNullReferenceException();
        }
        return this.dataSource.delete(id);
    }

    @Override
    public List<T> findAll() {
        return this.dataSource.findAll();
    }

    @Override
    public List<T> findAll(long offset, long limit) {
        return this.dataSource.findAll(offset, limit);
    }

    @Override
    public boolean existsById(Long id) {
        return this.dataSource.existsById(id);
    }

    @Override
    public long count() {
        return this.dataSource.count();
    }

    private void validateEntity(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            String messages = violations.stream()
                    .map( cv -> cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage() )
                    .collect( Collectors.joining( ", " ) );

            throw new EntityValidationException(messages);
        }
    }
}
