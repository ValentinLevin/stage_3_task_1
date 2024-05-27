package com.mjc.school.web.exception;

import com.mjc.school.web.constant.RESULT_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class NewsNotFoundWebException extends CustomWebException {
    private static final int HTTP_STATUS = SC_NOT_FOUND;
    private static final String MESSAGE_TEMPLATE = "Not found news with id %d";

    public NewsNotFoundWebException(long id) {
        super(
                RESULT_CODE.NEWS_NOT_FOUND,
                String.format(MESSAGE_TEMPLATE, id),
                HTTP_STATUS
        );
    }
}
