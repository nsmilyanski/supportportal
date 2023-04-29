package com.example.supportportal.exception.domain;

public class EmailExistException extends Exception{
    public EmailExistException() {
    }

    public EmailExistException(String message) {
        super(message);
    }
}
