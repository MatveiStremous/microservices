package com.example.libraryservice.service;

import java.util.List;

public interface LibraryService {
    void takeBook(Long bookId);

    void returnBook(Long bookId);

    List<Long> getBorrowedBooks();
}
