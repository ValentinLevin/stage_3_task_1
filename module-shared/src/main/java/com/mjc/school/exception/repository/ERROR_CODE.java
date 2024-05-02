package com.mjc.school.exception.repository;

public enum ERROR_CODE {
    DATA_FILE_NOT_FOUND(1),
    ILLEGAL_ARGUMENT_VALUE(2),
    ENTITY_VALIDATION_FAILED(3),
    DTO_VALIDATION_FAILED(4);

    private final int errorCode;

    public int getErrorCode() {
        return this.errorCode;
    }

    ERROR_CODE(int errorCode) {
        this.errorCode = errorCode;
    }
}
