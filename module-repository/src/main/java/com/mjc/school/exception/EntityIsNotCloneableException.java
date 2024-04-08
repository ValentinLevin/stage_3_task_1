package com.mjc.school.exception;

public class EntityIsNotCloneableException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Not implemented clone method in entity class (%s)";

    public EntityIsNotCloneableException(String className) {
        super(String.format(EXCEPTION_MESSAGE_TEMPLATE, className));
    }
}
