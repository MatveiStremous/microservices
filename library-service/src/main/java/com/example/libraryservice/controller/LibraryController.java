package com.example.libraryservice.controller;

import com.example.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("library")
@ApiResponse(responseCode = "200", description = "Successful operation")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/take/{bookId}")
    @Operation(summary = "Take a book", description = "Take a book from library")
    @ApiResponse(responseCode = "409", description = "This book is unavailable now")
    public void takeBook(@PathVariable
                         @Parameter(description = "ID of the book", in = ParameterIn.PATH) Long bookId) {
        libraryService.takeBook(bookId);
    }

    @PostMapping("/return/{bookId}")
    @Operation(summary = "Return a book", description = "Return a book to library (request to library service)")
    @ApiResponse(responseCode = "409", description = "This book has already been returned.")
    public void returnBook(@PathVariable
                           @Parameter(description = "ID of the book", in = ParameterIn.PATH) Long bookId) {
        libraryService.returnBook(bookId);
    }
}
