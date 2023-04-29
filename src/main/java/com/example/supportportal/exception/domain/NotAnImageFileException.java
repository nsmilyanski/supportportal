package com.example.supportportal.exception.domain;

public class NotAnImageFileException extends Exception{
    public NotAnImageFileException() {
    }

    public NotAnImageFileException(String message) {
        super(message);
    }
}
