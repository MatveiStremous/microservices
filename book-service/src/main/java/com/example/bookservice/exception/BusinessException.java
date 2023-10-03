package com.example.bookservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;
}

