package com.example.libraryservice.controller;

import com.example.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("library")
@ApiResponse(responseCode = "200", description = "Successful operation")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

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

    @GetMapping("/borrowed")
    @Operation(summary = "Return list of borrowed books", description = "Return list of books which are borrowed.")
    public List<Long> getBorrowedBooks() {
        return libraryService.getBorrowedBooks();
    }
}
