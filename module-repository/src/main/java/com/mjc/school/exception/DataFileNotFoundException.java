package com.mjc.school.exception;

public class DataFileNotFoundException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Not found data file with name %s";

    public DataFileNotFoundException(String filename) {
        super(String.format(EXCEPTION_MESSAGE_TEMPLATE, filename));
    }
}
