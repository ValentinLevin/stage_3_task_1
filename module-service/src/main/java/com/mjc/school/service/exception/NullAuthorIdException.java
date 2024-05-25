package com.mjc.school.service.exception;

public class NullAuthorIdException extends CustomServiceException {
    private static final String MESSAGE_TEMPLATE = "Incorrect author id value passed: null";

    public NullAuthorIdException() {
        super(MESSAGE_TEMPLATE);
    }
}
