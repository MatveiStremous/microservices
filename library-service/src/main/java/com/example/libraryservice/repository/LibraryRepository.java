package com.example.libraryservice.repository;

import com.example.libraryservice.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Record, Long> {
    Optional<Record> findTopByBookIdOrderByEndDateDesc(Long bookId);
}
