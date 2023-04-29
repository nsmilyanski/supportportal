package com.example.supportportal.exception.domain;

public class UsernameExistException extends Exception{
    public UsernameExistException() {
    }

    public UsernameExistException(String message) {
        super(message);
    }
}
