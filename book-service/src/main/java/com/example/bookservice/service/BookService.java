package com.example.bookservice.service;

import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;

    public BookService(ModelMapper modelMapper, BookRepository bookRepository) {
        this.modelMapper = modelMapper;
        this.bookRepository = bookRepository;
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book getById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(HttpStatus.BAD_REQUEST, "This book doesn't exist."));
    }

    public List<Book> getAllByIsbn(String isbn) {
        return bookRepository.findAllByIsbn(isbn);
    }

    public void addNewBook(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        bookRepository.save(book);
    }

    public void deleteById(Long id) {
        if (isBookExist(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(HttpStatus.BAD_REQUEST, "This book doesn't exist.");
        }
    }

    public void update(Long id, BookDTO bookDTO) {
        if (isBookExist(id)) {
            Book book = modelMapper.map(bookDTO, Book.class);
            book.setId(id);
            bookRepository.save(book);
        } else {
            throw new BookNotFoundException(HttpStatus.BAD_REQUEST, "This book doesn't exist.");
        }
    }

    private boolean isBookExist(Long bookId) {
        return bookRepository.findById(bookId).isPresent();
    }
}