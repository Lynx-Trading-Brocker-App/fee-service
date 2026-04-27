package com.lynx.fee_service.exception;

public class FeeNotFoundException extends RuntimeException {
    public FeeNotFoundException(String message) {
        super(message);
    }
}