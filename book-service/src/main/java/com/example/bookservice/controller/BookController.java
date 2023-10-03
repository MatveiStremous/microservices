package com.example.bookservice.controller;

import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.model.Book;
import com.example.bookservice.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(bookService.getById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public List<Book> getByIsbn(@PathVariable("isbn") String isbn) {
        return bookService.getAllByIsbn(isbn);
    }

    @PostMapping
    public void addNewBook(@RequestBody BookDTO bookDTO) {
        bookService.addNewBook(bookDTO);
    }

    @PutMapping("/{id}")
    public void updateById(@PathVariable("id") Long id, @RequestBody BookDTO bookDTO) {
        bookService.update(id, bookDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }
}
