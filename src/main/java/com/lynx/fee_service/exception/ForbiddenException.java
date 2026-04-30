package com.lynx.fee_service.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("Access denied");
    }
}