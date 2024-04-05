package com.mjc.school.exception;

public class UnsupportedModelException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Unsupported model class %s";

    public UnsupportedModelException(String filename) {
        super(String.format(EXCEPTION_MESSAGE_TEMPLATE, filename));
    }
}
