package com.mjc.school.web.exception;

import com.mjc.school.shared.exception.CustomException;
import com.mjc.school.web.constant.ERROR_CODE;

public class CustomWebException extends CustomException {
    private final int httpStatus;
    private final ERROR_CODE errorCode;

    public int getHttpStatus() {
        return httpStatus;
    }

    public ERROR_CODE getErrorCode() {
        return errorCode;
    }

    public CustomWebException(ERROR_CODE errorCode, String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
