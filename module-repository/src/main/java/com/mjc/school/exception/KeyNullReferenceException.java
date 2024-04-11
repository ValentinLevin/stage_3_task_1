package com.mjc.school.exception;

public class KeyNullReferenceException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Null was passed as an object to be saved";

    public KeyNullReferenceException() {
        super(String.format(EXCEPTION_MESSAGE_TEMPLATE));
    }
}
