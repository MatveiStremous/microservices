package com.example.bookservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Setter
@Getter
public class BookNotFoundException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
}
