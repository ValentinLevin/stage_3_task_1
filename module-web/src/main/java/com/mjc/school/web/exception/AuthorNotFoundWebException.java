package com.mjc.school.web.exception;

import com.mjc.school.web.constant.RESULT_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class AuthorNotFoundWebException extends CustomWebException {
    private static final int HTTP_STATUS = SC_BAD_REQUEST;
    private static final String MESSAGE_TEMPLATE = "Not found author with id %d";

    public AuthorNotFoundWebException(long id) {
        super(
                RESULT_CODE.AUTHOR_NOT_FOUND,
                String.format(MESSAGE_TEMPLATE, id),
                HTTP_STATUS
        );
    }
}
