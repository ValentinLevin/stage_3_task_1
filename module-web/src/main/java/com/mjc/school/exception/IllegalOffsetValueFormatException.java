package com.mjc.school.exception;

import com.mjc.school.constant.ERROR_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class IllegalOffsetValueFormatException extends CustomWebException {
    private static final int HTTP_STATUS = SC_BAD_REQUEST;
    private static final String INVALID_FORMAT_MESSAGE_TEMPLATE = "The offset value is in invalid %s";
    private static final String INVALID_VALUE_MESSAGE_TEMPLATE = "Received value of offset is %d. The offset value must be greater than or equals to zero";

    public IllegalOffsetValueFormatException(String offsetValue) {
        super(
                ERROR_CODE.ILLEGAL_REQUEST_DATA_FORMAT,
                String.format(INVALID_FORMAT_MESSAGE_TEMPLATE, offsetValue),
                HTTP_STATUS
        );
    }

    public IllegalOffsetValueFormatException(long offsetValue) {
        super(
                ERROR_CODE.ILLEGAL_REQUEST_DATA_FORMAT,
                String.format(INVALID_VALUE_MESSAGE_TEMPLATE, offsetValue),
                HTTP_STATUS
        );
    }
}
