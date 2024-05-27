package com.mjc.school.web.constant;

import lombok.Getter;

import static jakarta.servlet.http.HttpServletResponse.*;

@Getter
public enum RESULT_CODE {
    SUCCESS(0, SC_OK),
    ADD_SUCCESS(0, SC_CREATED),
    UNEXPECTED_ERROR(1, SC_INTERNAL_SERVER_ERROR),
    NEWS_NOT_FOUND(2, SC_NOT_FOUND),
    AUTHOR_NOT_FOUND(3, SC_BAD_REQUEST),
    REQUEST_IS_NOT_IN_UTF_ENCODING(4, SC_BAD_REQUEST),
    ILLEGAL_REQUEST_DATA_FORMAT(5, SC_BAD_REQUEST),
    DATA_VALIDATION(6, SC_BAD_REQUEST),
    ILLEGAL_ID_VALUE(7, SC_BAD_REQUEST);

    private final int errorCode;
    private final int httpStatus;

    public boolean isSuccess() {
        return this == SUCCESS;
    }

    RESULT_CODE(int errorCode, int httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
