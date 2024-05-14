package com.mjc.school.exception;

import com.mjc.school.constant.ERROR_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class RequestBodyReadException extends CustomWebException {
    private static final int HTTP_STATUS = SC_INTERNAL_SERVER_ERROR;
    private static final String MESSAGE_TEMPLATE = "Error reading request data from body";

    public RequestBodyReadException() {
        super(
                ERROR_CODE.REQUEST_BODY_READ,
                MESSAGE_TEMPLATE,
                HTTP_STATUS
        );
    }
}
