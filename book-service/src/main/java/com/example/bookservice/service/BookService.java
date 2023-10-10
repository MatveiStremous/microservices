package com.example.bookservice.service;

import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;

    @Value("${library_service_uri}")
    private String libraryServiceURI;

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

    public void takeBook(Long bookId) {
        if (isBookExist(bookId)) {
            String uri = libraryServiceURI + "/take/{id}";
            restTemplate.postForEntity(uri, null, String.class, bookId);
        } else {
            throw new BookNotFoundException(HttpStatus.BAD_REQUEST, "This book doesn't exist.");
        }
    }

    public void returnBook(Long bookId) {
        if (isBookExist(bookId)) {
            String uri = libraryServiceURI + "/return/{id}";
            restTemplate.postForEntity(uri, null, String.class, bookId);
        } else {
            throw new BookNotFoundException(HttpStatus.BAD_REQUEST, "This book doesn't exist.");
        }
    }

    private boolean isBookExist(Long bookId) {
        return bookRepository.findById(bookId).isPresent();
    }
}
