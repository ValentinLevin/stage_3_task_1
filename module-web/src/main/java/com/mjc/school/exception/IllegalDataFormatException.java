package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.exception.repository.ERROR_CODE;

public class IllegalDataFormatException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "Error in request data format for class %s";
    private static final String MESSAGE_TEMPLATE_WITH_DATA = "Error in request data format for class %s, received data %s";

    public IllegalDataFormatException(Class<?> clazz) {
        super(
                ERROR_CODE.ILLEGAL_REQUEST_DATA_FORMAT,
                String.format(MESSAGE_TEMPLATE, clazz.getCanonicalName())
        );
    }

    public IllegalDataFormatException(String data, Class<?> clazz) {
        super(
                ERROR_CODE.ILLEGAL_REQUEST_DATA_FORMAT,
                String.format(MESSAGE_TEMPLATE_WITH_DATA, clazz.getCanonicalName(), data)
        );
    }
}
