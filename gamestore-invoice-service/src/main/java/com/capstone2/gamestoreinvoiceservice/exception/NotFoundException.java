package com.capstone2.gamestoreinvoiceservice.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {}
    public NotFoundException(String message) {
        super(message);
    }
}
