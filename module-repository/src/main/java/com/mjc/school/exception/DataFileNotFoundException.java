package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;

import java.io.Serial;

public class DataFileNotFoundException extends CustomException {
    @Serial
    private static final long serialVersionUID = 2373539337608624346L;

    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Not found data file with name %s";

    public DataFileNotFoundException(String filename) {
        super(String.format(EXCEPTION_MESSAGE_TEMPLATE, filename));
    }
}
