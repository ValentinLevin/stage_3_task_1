package com.mjc.school.web.exception;

import com.mjc.school.web.constant.ERROR_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class IllegalLimitValueException extends CustomWebException {
    private static final int HTTP_STATUS = SC_BAD_REQUEST;
    private static final String INVALID_FORMAT_MESSAGE_TEMPLATE = "The limit value is in invalid %s";
    private static final String INVALID_VALUE_MESSAGE_TEMPLATE =
            "Received limit value is %d. The limit value must be greater than or equal to zero";

    public IllegalLimitValueException(String limitValue) {
        super(
                ERROR_CODE.ILLEGAL_REQUEST_DATA_FORMAT,
                String.format(INVALID_FORMAT_MESSAGE_TEMPLATE, limitValue),
                HTTP_STATUS
        );
    }

    public IllegalLimitValueException(Integer limitValue) {
        super(
                ERROR_CODE.ILLEGAL_REQUEST_DATA_FORMAT,
                String.format(INVALID_VALUE_MESSAGE_TEMPLATE, limitValue),
                HTTP_STATUS
        );
    }
}
