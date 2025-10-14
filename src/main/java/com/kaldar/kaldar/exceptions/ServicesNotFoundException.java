package com.kaldar.kaldar.exceptions;

import org.springframework.http.HttpStatus;

public class ServicesNotFoundException extends RuntimeException{
    public ServicesNotFoundException(String message) {
        super(message);
    }
}
