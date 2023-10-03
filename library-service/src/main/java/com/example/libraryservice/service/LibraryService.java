package com.example.libraryservice.service;

import com.example.libraryservice.exception.BusinessException;
import com.example.libraryservice.model.Record;
import com.example.libraryservice.repository.LibraryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class LibraryService {
    public final LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    private boolean isBookFree(Long bookId) {
        Optional<Record> record = libraryRepository.findTopByBookIdOrderByEndDateDesc(bookId);
        return record.isEmpty() || record.get().getEndDate().isBefore(LocalDate.now());
    }

    public void takeBook(Long bookId) {
        if (isBookFree(bookId)) {
            Record record = Record.builder()
                    .bookId(bookId)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(1))
                    .build();
            libraryRepository.save(record);
        } else {
            throw new BusinessException(HttpStatus.CONFLICT, "This book is unavailable now.");
        }
    }

    public void returnBook(Long bookId) {
        Optional<Record> record = libraryRepository.findTopByBookIdOrderByEndDateDesc(bookId);
        if (record.isPresent()) {
            if(record.get().getReturnDate() == null) {
                record.get().setReturnDate(LocalDate.now());
                libraryRepository.save(record.get());
            }else{
                throw new BusinessException(HttpStatus.CONFLICT, "This book has already been returned.");
            }
        } else {
            throw new BusinessException(HttpStatus.CONFLICT, "This book wasn't taken.");
        }
    }
}