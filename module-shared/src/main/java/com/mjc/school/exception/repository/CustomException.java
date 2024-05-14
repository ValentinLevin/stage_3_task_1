package com.mjc.school.exception.repository;

import java.io.Serial;

public class CustomException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8208852846725275315L;

    public CustomException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public CustomException(String errorMessage) {
        super(errorMessage);
    }
}
