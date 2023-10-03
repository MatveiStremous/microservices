package com.example.libraryservice.controller;

import com.example.libraryservice.service.LibraryService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/take/{bookId}")
    public void takeBook(@PathVariable Long bookId) {
        libraryService.takeBook(bookId);
    }

    @PostMapping("/return/{bookId}")
    public void returnBook(@PathVariable Long bookId) {
        libraryService.returnBook(bookId);
    }
}
