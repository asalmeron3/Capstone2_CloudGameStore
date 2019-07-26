package com.capstone2.gamestorecustomerservice.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {}
    public NotFoundException(String message) {
        super(message);
    }
}
