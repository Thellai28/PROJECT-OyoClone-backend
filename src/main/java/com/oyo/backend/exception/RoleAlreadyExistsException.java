package com.oyo.backend.exception;

public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException( String message ) {
        super(message);
    }
}
