package com.example.bookservice.service.impl;

import com.example.bookservice.dto.BookRequest;
import com.example.bookservice.dto.BookResponse;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.exception.BusinessException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final String BOOK_DOESNT_EXIST = "This book doesn't exist.";
    private final String BOOK_WITH_THIS_ISBN_ALREADY_EXISTS = "Book with this isbn already exists.";
    private final String TAKE_BOOK_URL = "/take/{id}";
    private final String RETURN_BOOK_URL = "/return/{id}";
    private final String GET_BORROWED_BOOKS_URL = "/borrowed";

    @Value("${library_service_uri}")
    private String libraryServiceURI;

    public List<BookResponse> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(x -> modelMapper.map(x, BookResponse.class))
                .collect(Collectors.toList());
    }

    public BookResponse getById(Long id) {
        Book foundBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(HttpStatus.BAD_REQUEST, BOOK_DOESNT_EXIST));
        return modelMapper.map(foundBook, BookResponse.class);
    }

    public BookResponse getByIsbn(String isbn) {
        Book foundBook = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(HttpStatus.BAD_REQUEST, BOOK_DOESNT_EXIST));
        return modelMapper.map(foundBook, BookResponse.class);
    }

    public BookResponse addNewBook(BookRequest bookRequest) {
        Book book = modelMapper.map(bookRequest, Book.class);
        if (isBookExist(book.getIsbn())) {
            throw new BusinessException(HttpStatus.CONFLICT, BOOK_WITH_THIS_ISBN_ALREADY_EXISTS);
        }
        return modelMapper.map(bookRepository.save(book), BookResponse.class);
    }

    public void deleteById(Long id) {
        if (!isBookExist(id)) {
            throw new BookNotFoundException(HttpStatus.BAD_REQUEST, BOOK_DOESNT_EXIST);
        }
        bookRepository.deleteById(id);
    }

    public BookResponse updateById(Long id, BookRequest bookRequest) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(HttpStatus.BAD_REQUEST, BOOK_DOESNT_EXIST));

        if (!existingBook.getIsbn().equals(bookRequest.getIsbn()) && isBookExist(bookRequest.getIsbn())) {
            throw new BusinessException(HttpStatus.CONFLICT, BOOK_WITH_THIS_ISBN_ALREADY_EXISTS);
        }
        Book updatedBook = modelMapper.map(bookRequest, Book.class);
        updatedBook.setId(id);
        return modelMapper.map(bookRepository.save(updatedBook), BookResponse.class);
    }

    public BookResponse takeBook(Long bookId) {
        if (isBookExist(bookId)) {
            String uri = libraryServiceURI + TAKE_BOOK_URL;
            restTemplate.postForEntity(uri, null, String.class, bookId);
            return getById(bookId);
        } else {
            throw new BookNotFoundException(HttpStatus.BAD_REQUEST, BOOK_DOESNT_EXIST);
        }
    }

    public void returnBook(Long bookId) {
        if (isBookExist(bookId)) {
            String uri = libraryServiceURI + RETURN_BOOK_URL;
            restTemplate.postForEntity(uri, null, String.class, bookId);
        } else {
            throw new BookNotFoundException(HttpStatus.BAD_REQUEST, BOOK_DOESNT_EXIST);
        }
    }

    public List<BookResponse> getFreeBooks() {
        String uri = libraryServiceURI + GET_BORROWED_BOOKS_URL;
        ResponseEntity<List<Long>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        List<Long> borrowedBooksIds = response.getBody();
        if (borrowedBooksIds == null || borrowedBooksIds.isEmpty()) {
            return getAll();
        }
        return getAll().stream()
                .filter(bookResponse -> !borrowedBooksIds.contains(bookResponse.getId()))
                .collect(Collectors.toList());
    }

    private boolean isBookExist(Long bookId) {
        return bookRepository.findById(bookId).isPresent();
    }

    private boolean isBookExist(String isbn) {
        return bookRepository.findByIsbn(isbn).isPresent();
    }
}
