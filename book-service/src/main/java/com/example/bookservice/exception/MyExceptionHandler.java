package com.example.bookservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessException(BusinessException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getMessage()));
    }


    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(BookNotFoundException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getMessage()));
    }
}
