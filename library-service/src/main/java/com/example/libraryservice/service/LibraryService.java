package com.example.libraryservice.service;

public interface LibraryService {
    void takeBook(Long bookId);

    void returnBook(Long bookId);
}
