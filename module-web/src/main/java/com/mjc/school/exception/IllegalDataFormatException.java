package com.mjc.school.exception;

import com.mjc.school.exception.repository.ERROR_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class IllegalDataFormatException extends CustomWebException {
    private static final int HTTP_STATUS = SC_BAD_REQUEST;
    private static final String MESSAGE_TEMPLATE = "Error in request data format for class %s";
    private static final String MESSAGE_TEMPLATE_WITH_DATA = "Error in request data format for class %s, received data %s";

    public IllegalDataFormatException(Class<?> clazz) {
        super(
                ERROR_CODE.ILLEGAL_REQUEST_DATA_FORMAT,
                String.format(MESSAGE_TEMPLATE, clazz.getCanonicalName()),
                HTTP_STATUS
        );
    }

    public IllegalDataFormatException(String data, Class<?> clazz) {
        super(
                ERROR_CODE.ILLEGAL_REQUEST_DATA_FORMAT,
                String.format(MESSAGE_TEMPLATE_WITH_DATA, clazz.getCanonicalName(), data)
                HTTP_STATUS
        );
    }
}
