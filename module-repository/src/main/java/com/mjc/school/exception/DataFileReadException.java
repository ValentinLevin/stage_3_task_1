package com.mjc.school.exception;

public class DataFileReadException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Error on read data file with name %s";

    public DataFileReadException(String filename, Throwable e) {
        super(String.format(EXCEPTION_MESSAGE_TEMPLATE, filename), e);
    }
}
