package com.mjc.school.web.exception;

import com.mjc.school.web.constant.RESULT_CODE;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class DataValidationWebException extends CustomWebException {
    private static final int HTTP_STATUS = SC_BAD_REQUEST;

    public DataValidationWebException(String validationErrors) {
        super(
                RESULT_CODE.DATA_VALIDATION,
                validationErrors,
                HTTP_STATUS
        );
    }
}
