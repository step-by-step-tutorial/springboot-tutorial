package com.tutorial.springboot.security_authentication_inmemory.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String username) {
        super(username);
    }
}
