package com.mjc.school.service.exception;

import com.mjc.school.shared.exception.CustomException;

public class AuthorNotFoundException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "Author with id %d not found";

    public AuthorNotFoundException(long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
