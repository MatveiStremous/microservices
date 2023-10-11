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
            Record newRecord = Record.builder()
                    .bookId(bookId)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(BOOK_ISSUED_DAYS_NUMBER))
                    .build();
            libraryRepository.save(newRecord);
        } else {
            throw new BusinessException(HttpStatus.CONFLICT, BOOK_UNAVAILABLE_NOW);
        }
    }

    public void returnBook(Long bookId) {
        Optional<Record> existingRecord = libraryRepository.findTopByBookIdOrderByEndDateDesc(bookId);
        if (existingRecord.isPresent()) {
            if (existingRecord.get().getReturnDate() == null) {
                existingRecord.get().setReturnDate(LocalDate.now());
                libraryRepository.save(existingRecord.get());
            } else {
                throw new BusinessException(HttpStatus.CONFLICT, BOOK_ALREADY_RETURNED);
            }
        } else {
            throw new BusinessException(HttpStatus.CONFLICT, BOOK_WASNT_TAKEN);
        }
    }
}