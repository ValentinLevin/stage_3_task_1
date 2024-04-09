package com.mjc.school.exception;

import com.mjc.school.model.Entity;

public class UnsupportedEntityClassException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Unsupported model class %s";

    public UnsupportedEntityClassException(Class entityClass) {
        super(String.format(EXCEPTION_MESSAGE_TEMPLATE, entityClass.getCanonicalName()));
    }
}
