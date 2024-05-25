package com.mjc.school.service.exception;

public class NullNewsIdException extends CustomServiceException {
    private static final String MESSAGE_TEMPLATE = "Incorrect news id value passed: null";

    public NullNewsIdException() {
        super(MESSAGE_TEMPLATE);
    }
}
