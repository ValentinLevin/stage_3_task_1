package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.exception.repository.ERROR_CODE;

public class DataFileNotFoundException extends CustomException {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Not found data file with name %s";

    public DataFileNotFoundException(String filename) {
        super(ERROR_CODE.DATA_FILE_NOT_FOUND, String.format(EXCEPTION_MESSAGE_TEMPLATE, filename));
    }
}
