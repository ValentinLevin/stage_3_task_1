package com.mjc.school.exception.repository;

public class UnexpectedErrorException extends CustomException {
    public UnexpectedErrorException(Throwable throwable) {
        super(throwable.getMessage());
    }
}
