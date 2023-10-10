package com.example.libraryservice.service;

import com.example.libraryservice.exception.BusinessException;
import com.example.libraryservice.model.Record;
import com.example.libraryservice.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryService {
    public final LibraryRepository libraryRepository;
    private final Integer BOOK_ISSUED_DAYS_NUMBER = 30;
    private final String BOOK_UNAVAILABLE_NOW = "This book is unavailable now.";
    private final String BOOK_WASNT_TAKEN = "This book wasn't taken.";
    private final String BOOK_ALREADY_RETURNED = "This book has already been returned.";

    private boolean isBookFree(Long bookId) {
        Optional<Record> record = libraryRepository.findTopByBookIdOrderByEndDateDesc(bookId);
        return record.isEmpty() || record.get().getEndDate().isBefore(LocalDate.now());
    }

    public void takeBook(Long bookId) {
        if (isBookFree(bookId)) {
            Record record = Record.builder()
                    .bookId(bookId)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(BOOK_ISSUED_DAYS_NUMBER))
                    .build();
            libraryRepository.save(record);
        } else {
            throw new BusinessException(HttpStatus.CONFLICT, BOOK_UNAVAILABLE_NOW);
        }
    }

    public void returnBook(Long bookId) {
        Optional<Record> record = libraryRepository.findTopByBookIdOrderByEndDateDesc(bookId);
        if (record.isPresent()) {
            if (record.get().getReturnDate() == null) {
                record.get().setReturnDate(LocalDate.now());
                libraryRepository.save(record.get());
            } else {
                throw new BusinessException(HttpStatus.CONFLICT, BOOK_ALREADY_RETURNED);
            }
        } else {
            throw new BusinessException(HttpStatus.CONFLICT, BOOK_WASNT_TAKEN);
        }
    }
}