package com.mjc.school.service.exception;

import java.io.Serial;

public class CustomServiceRuntimeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5844686610864219067L;

    public CustomServiceRuntimeException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public CustomServiceRuntimeException(String errorMessage) {
        super(errorMessage);
    }
}
