package com.mjc.school.exception;

import com.mjc.school.constant.ERROR_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class UnexceptedException extends CustomWebException {
    private static final int HTTP_STATUS = SC_INTERNAL_SERVER_ERROR;
    private static final String MESSAGE_TEMPLATE = "При обработке запроса произошла непредвиденная ошибка (%s)";

    public UnexceptedException(Exception e) {
        super(
                ERROR_CODE.UNEXPECTED_ERROR,
                String.format(MESSAGE_TEMPLATE, e.getMessage()),
                HTTP_STATUS
        );
    }
}
