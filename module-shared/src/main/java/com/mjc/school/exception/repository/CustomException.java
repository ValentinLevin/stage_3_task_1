package com.mjc.school.exception.repository;

import java.io.Serial;

public class CustomException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8208852846725275315L;

    private final ERROR_CODE errorCode;

    public CustomException(ERROR_CODE errorCode, String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
        this.errorCode = errorCode;
    }

    public CustomException(ERROR_CODE errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public ERROR_CODE getErrorCode() {
        return this.errorCode;
    }
}
