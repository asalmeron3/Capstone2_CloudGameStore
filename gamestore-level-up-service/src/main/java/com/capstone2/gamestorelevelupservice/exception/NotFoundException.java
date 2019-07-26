package com.capstone2.gamestorelevelupservice.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {}
    public NotFoundException(String message) {
        super(message);
    }
}
