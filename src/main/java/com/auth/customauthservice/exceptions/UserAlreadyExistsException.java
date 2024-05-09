package com.auth.customauthservice.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String s) {
        super(s);
    }
}
