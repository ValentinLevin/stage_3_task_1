package com.mjc.school.web.exception;

import com.mjc.school.web.constant.ERROR_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class UnexpectedErrorException extends CustomWebException {
    private static final int HTTP_STATUS = SC_INTERNAL_SERVER_ERROR;
    private static final String MESSAGE_TEMPLATE = "При обработке запроса произошла непредвиденная ошибка (%s)";

    public UnexpectedErrorException(Exception e) {
        super(
                ERROR_CODE.UNEXPECTED_ERROR,
                String.format(MESSAGE_TEMPLATE, e.getMessage()),
                HTTP_STATUS
        );
    }
}
