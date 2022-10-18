package org.example.controllers;

import org.example.exceptions.AppInsideLoginException;
import org.example.exceptions.TokenValidateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(AppInsideLoginException.class)
    public ResponseEntity<?> handleLoginException(AppInsideLoginException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(TokenValidateException.class)
    public ResponseEntity<?> handleLoginException(TokenValidateException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }
}
