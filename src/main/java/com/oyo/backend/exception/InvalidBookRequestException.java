package com.oyo.backend.exception;


import org.springframework.web.bind.annotation.GetMapping;

public class InvalidBookRequestException extends RuntimeException {
    public InvalidBookRequestException( String message ){
        super( message );
    }
}
