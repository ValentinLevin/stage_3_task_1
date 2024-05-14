package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.constant.ERROR_CODE;

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
