package com.mjc.school.exception;

public enum ERROR_CODE {
    DATA_RILE_NOT_FOUND(1);

    private final int errorCode;

    public int getErrorCode() {
        return this.errorCode;
    }

    ERROR_CODE(int errorCode) {
        this.errorCode = errorCode;
    }
}
