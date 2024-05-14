package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;

public class DTOValidationException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "The following errors were detected during the check: %s";

    public DTOValidationException(String constrainViolations) {
        super(String.format(MESSAGE_TEMPLATE, constrainViolations));
    }
}
