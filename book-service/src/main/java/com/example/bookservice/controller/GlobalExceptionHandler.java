package com.example.bookservice.controller;

import com.example.bookservice.dto.ErrorResponse;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
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
