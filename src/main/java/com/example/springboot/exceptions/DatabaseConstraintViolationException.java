package com.example.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DatabaseConstraintViolationException extends RuntimeException {
    public DatabaseConstraintViolationException(String message) {
        super(message);
    }
}
