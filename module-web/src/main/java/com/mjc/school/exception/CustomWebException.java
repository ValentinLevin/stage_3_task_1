package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.exception.repository.ERROR_CODE;

public class CustomWebException extends CustomException {
    private final int httpStatus;

    public int getHttpStatus() {
        return httpStatus;
    }

    public CustomWebException(ERROR_CODE errorCode, String message, int httpStatus) {
        super(errorCode, message);
        this.httpStatus = httpStatus;
    }
}
