package com.mjc.school.service.exception;

public class NewsNotFoundException extends CustomServiceException {
    private static final String MESSAGE_TEMPLATE = "Not found news with id %d";

    public NewsNotFoundException(long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
