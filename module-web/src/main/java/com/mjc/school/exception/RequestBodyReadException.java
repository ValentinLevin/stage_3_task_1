package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.exception.repository.ERROR_CODE;

public class RequestBodyReadException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "Error reading request data from body";

    public RequestBodyReadException() {
        super(ERROR_CODE.REQUEST_BODY_READ, MESSAGE_TEMPLATE);
    }
}
