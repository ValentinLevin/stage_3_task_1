package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.exception.repository.ERROR_CODE;

public class EntityValidationException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "The following errors were detected during the check: %s";

    public EntityValidationException(String constrainViolation) {
        super(ERROR_CODE.ENTITY_VALIDATION_FAILED , String.format(MESSAGE_TEMPLATE, constrainViolation));
    }
}
