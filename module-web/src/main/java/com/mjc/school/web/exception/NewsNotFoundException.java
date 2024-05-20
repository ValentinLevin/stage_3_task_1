package com.mjc.school.web.exception;

import com.mjc.school.web.constant.ERROR_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class NewsNotFoundException extends CustomWebException {
    private static final int HTTP_STATUS = SC_NOT_FOUND;
    private static final String MESSAGE_TEMPLATE = "Not found news with id %d";

    public NewsNotFoundException(long id) {
        super(
                ERROR_CODE.NEWS_NOT_FOUND,
                String.format(MESSAGE_TEMPLATE, id),
                HTTP_STATUS
        );
    }
}
