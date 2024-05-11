package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.exception.repository.ERROR_CODE;

public class NotUTFEncodingException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "The message must be in UTF-8 encoding";

    public NotUTFEncodingException() {
        super(ERROR_CODE.REQUEST_IS_NOT_IN_UTF_ENCODING, MESSAGE_TEMPLATE);
    }
}
