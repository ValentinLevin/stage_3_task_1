package com.mjc.school.web.constant;

import lombok.Getter;

import static jakarta.servlet.http.HttpServletResponse.*;

@Getter
public enum ERROR_CODE {
    NO_ERROR(0, SC_OK),
    UNEXPECTED_ERROR(1, SC_INTERNAL_SERVER_ERROR),
    NEWS_NOT_FOUND(2, SC_NOT_FOUND),
    AUTHOR_NOT_FOUND(3, SC_BAD_REQUEST),
    REQUEST_IS_NOT_IN_UTF_ENCODING(4, SC_BAD_REQUEST),
    ILLEGAL_REQUEST_DATA_FORMAT(5, SC_BAD_REQUEST),
    DATA_VALIDATION(6, SC_BAD_REQUEST),
    ILLEGAL_ID_VALUE(7, SC_BAD_REQUEST);

    private final int id;
    private final int httpStatus;

    ERROR_CODE(int id, int httpStatus) {
        this.id = id;
        this.httpStatus = httpStatus;
    }
}
