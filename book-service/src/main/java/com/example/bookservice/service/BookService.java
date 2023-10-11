package com.example.bookservice.service;

import com.example.bookservice.dto.BookRequest;
import com.example.bookservice.dto.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse> getAll();

    BookResponse getById(Long id);

    BookResponse getByIsbn(String isbn);

    BookResponse addNewBook(BookRequest bookRequest);

    void deleteById(Long id);

    BookResponse updateById(Long id, BookRequest bookRequest);

    BookResponse takeBook(Long bookId);

    void returnBook(Long bookId);

    List<BookResponse> getFreeBooks();
}
