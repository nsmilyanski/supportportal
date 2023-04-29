package com.example.supportportal.exception.domain;

public class EmailNotFoundException extends Exception{
    public EmailNotFoundException() {
    }

    public EmailNotFoundException(String message) {
        super(message);
    }
}
