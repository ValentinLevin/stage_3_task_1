package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.exception.repository.ERROR_CODE;

public class IllegalArgumentException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "Invalid value specified for %s parameter %s";

    public IllegalArgumentException(String parameterName, String parameterValue) {
        super(
                ERROR_CODE.ILLEGAL_ARGUMENT_VALUE,
                String.format(MESSAGE_TEMPLATE, parameterName, parameterValue)
        );
    }
}
