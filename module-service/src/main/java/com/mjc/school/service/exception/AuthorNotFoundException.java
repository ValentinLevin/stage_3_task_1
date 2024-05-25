package com.mjc.school.service.exception;

public class AuthorNotFoundException extends CustomServiceException {
    private static final String MESSAGE_TEMPLATE = "Author with id %d not found";

    public AuthorNotFoundException(long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
