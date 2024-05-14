package com.mjc.school.constant;

public enum ERROR_CODE {
    NO_ERROR(0),
    UNEXPECTED_ERROR(1),
    DATA_FILE_NOT_FOUND(1),
    ILLEGAL_ARGUMENT_VALUE(2),
    ENTITY_VALIDATION_FAILED(3),
    DTO_VALIDATION_FAILED(4),
    REQUEST_IS_NOT_IN_UTF_ENCODING(5),
    REQUEST_BODY_READ(6),
    ILLEGAL_REQUEST_DATA_FORMAT(7);

    private final int id;

    public int getId() {
        return this.id;
    }

    ERROR_CODE(int id) {
        this.id = id;
    }
}
