package com.capstone2.gamestoreproductservice.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(){}
    public NotFoundException(String message) {
        super(message);
    }
}
