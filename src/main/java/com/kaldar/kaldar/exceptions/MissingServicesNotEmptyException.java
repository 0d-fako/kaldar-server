package com.kaldar.kaldar.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class MissingServicesNotEmptyException extends RuntimeException {
    public MissingServicesNotEmptyException(String message) {
        super(message);

    }
}
