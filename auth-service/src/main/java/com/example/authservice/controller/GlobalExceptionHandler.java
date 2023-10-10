package com.example.authservice.controller;

import com.example.authservice.dto.ErrorResponse;
import com.example.authservice.exception.BusinessException;
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
}
