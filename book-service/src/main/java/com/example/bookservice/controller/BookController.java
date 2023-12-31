package com.example.bookservice.controller;

import com.example.bookservice.dto.BookRequest;
import com.example.bookservice.dto.BookResponse;
import com.example.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
@ApiResponse(responseCode = "401", description = "Authentication failed")
@ApiResponse(responseCode = "200", description = "Successful operation")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Returns a list of books")
    public List<BookResponse> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Returns a book by its ID")
    @ApiResponse(responseCode = "400", description = "This book doesn't exist")
    public BookResponse getById(@PathVariable("id")
                                @Parameter(description = "ID of the book", in = ParameterIn.PATH) Long id) {
        return bookService.getById(id);
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Get a book by ISBN", description = "Returns a books by its ISBN")
    public BookResponse getByIsbn(@PathVariable("isbn")
                                  @Parameter(description = "ISBN of the book", in = ParameterIn.PATH) String isbn) {
        return bookService.getByIsbn(isbn);
    }

    @PostMapping
    @Operation(summary = "Add a new book", description = "Adds a new book to the library")
    public BookResponse addNewBook(@RequestBody
                                   @Parameter(description = "Book data", in = ParameterIn.DEFAULT) BookRequest bookRequest) {
        return bookService.addNewBook(bookRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book by ID", description = "Updates a book's information by its ID")
    @ApiResponse(responseCode = "400", description = "This book doesn't exist")
    public BookResponse updateById(@PathVariable("id")
                                   @Parameter(description = "ID of the book", in = ParameterIn.PATH) Long id,
                                   @RequestBody
                                   @Parameter(description = "Updated book data", in = ParameterIn.DEFAULT) BookRequest bookRequest) {
        return bookService.updateById(id, bookRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by ID", description = "Deletes a book by its ID")
    @ApiResponse(responseCode = "400", description = "This book doesn't exist")
    public void deleteById(@PathVariable("id")
                           @Parameter(description = "ID of the book", in = ParameterIn.PATH) Long id) {
        bookService.deleteById(id);
    }

    @PostMapping("/take/{bookId}")
    @Operation(summary = "Take a book", description = "Take a book from library (request to library service)")
    @ApiResponse(responseCode = "400", description = "This book doesn't exist")
    @ApiResponse(responseCode = "409", description = "This book is unavailable now")
    public BookResponse takeBook(@PathVariable
                                 @Parameter(description = "ID of the book", in = ParameterIn.PATH) Long bookId) {
        return bookService.takeBook(bookId);
    }

    @PostMapping("/return/{bookId}")
    @Operation(summary = "Return a book", description = "Return a book to library (request to library service)")
    @ApiResponse(responseCode = "400", description = "This book doesn't exist")
    @ApiResponse(responseCode = "409", description = "This book has already been returned.")
    public void returnBook(@PathVariable
                           @Parameter(description = "ID of the book", in = ParameterIn.PATH) Long bookId) {
        bookService.returnBook(bookId);
    }

    @GetMapping("/free")
    @Operation(summary = "Return list of free books", description = "Return list of books which are free.")
    public List<BookResponse> getFreeBooks() {
        return bookService.getFreeBooks();
    }
}
