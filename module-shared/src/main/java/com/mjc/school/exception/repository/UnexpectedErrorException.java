package com.mjc.school.exception.repository;

public class UnexpectedErrorException extends CustomException {
    public UnexpectedErrorException(Throwable throwable) {
        super(ERROR_CODE.UNEXPECTED_ERROR, throwable.getMessage());
    }
}
