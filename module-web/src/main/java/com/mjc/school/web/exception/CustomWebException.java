package com.mjc.school.web.exception;

import com.mjc.school.web.constant.ERROR_CODE;
import lombok.Getter;

@Getter
public class CustomWebException extends Exception {
    private final int httpStatus;
    private final ERROR_CODE errorCode;

    public CustomWebException(ERROR_CODE errorCode, String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
