package com.mjc.school.web.exception;

import com.mjc.school.web.constant.ERROR_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class AuthorNotFoundException extends CustomWebException {
    private static final int HTTP_STATUS = SC_NOT_FOUND;
    private static final String MESSAGE_TEMPLATE = "Not found author with id %d";

    public AuthorNotFoundException(long id) {
        super(
                ERROR_CODE.AUTHOR_NOT_FOUND,
                String.format(MESSAGE_TEMPLATE, id),
                HTTP_STATUS
        );
    }
}
