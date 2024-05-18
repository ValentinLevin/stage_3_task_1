package com.mjc.school.exception;

import com.mjc.school.exception.repository.CustomException;

public class NewsNotFoundException extends CustomException {
    private static final String MESSAGE_TEMPLATE = "Not found news with id %d";

    public NewsNotFoundException(long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
