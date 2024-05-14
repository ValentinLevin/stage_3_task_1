package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;

public class EntityValidationException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "The following errors were detected during the check: %s";

    public EntityValidationException(String constrainViolation) {
        super(String.format(MESSAGE_TEMPLATE, constrainViolation));
    }
}
