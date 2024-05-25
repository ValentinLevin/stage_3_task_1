package com.mjc.school.service.exception;

public class DTOValidationException extends CustomServiceException {
    private static final String MESSAGE_TEMPLATE = "The following errors were detected during the check: %s";

    public DTOValidationException(String constrainViolations) {
        super(String.format(MESSAGE_TEMPLATE, constrainViolations));
    }
}
