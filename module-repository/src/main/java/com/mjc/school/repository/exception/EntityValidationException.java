package com.mjc.school.repository.exception;

import com.mjc.school.shared.exception.CustomException;

public class EntityValidationException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "The following errors were detected during the check: %s";

    public EntityValidationException(String constrainViolation) {
        super(String.format(MESSAGE_TEMPLATE, constrainViolation));
    }
}
