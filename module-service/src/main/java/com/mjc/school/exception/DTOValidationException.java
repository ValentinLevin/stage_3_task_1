package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.exception.repository.ERROR_CODE;

public class DTOValidationException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "The following errors were detected during the check: %s";

    public DTOValidationException(String constrainViolations) {
        super(ERROR_CODE.DTO_VALIDATION_FAILED , String.format(MESSAGE_TEMPLATE, constrainViolations));
    }
}
