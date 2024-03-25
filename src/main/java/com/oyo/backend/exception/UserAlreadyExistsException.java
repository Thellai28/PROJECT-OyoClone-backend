package com.oyo.backend.exception;

import com.oyo.backend.model.User;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException( String message ){
        super( message);
    }
}
