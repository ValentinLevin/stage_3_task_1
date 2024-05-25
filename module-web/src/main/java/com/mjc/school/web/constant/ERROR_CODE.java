package com.mjc.school.web.constant;

public enum ERROR_CODE {
    NO_ERROR(0),
    UNEXPECTED_ERROR(1),
    NEWS_NOT_FOUND(2),
    AUTHOR_NOT_FOUND(3),
    REQUEST_IS_NOT_IN_UTF_ENCODING(4),
    ILLEGAL_REQUEST_DATA_FORMAT(5),
    DATA_VALIDATION(6),
    ILLEGAL_ID_VALUE(7);

    private final int id;

    public int getId() {
        return this.id;
    }

    ERROR_CODE(int id) {
        this.id = id;
    }
}
