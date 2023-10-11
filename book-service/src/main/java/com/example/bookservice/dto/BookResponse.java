package com.example.bookservice.dto;

import com.example.bookservice.model.Genre;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private String isbn;
    private String title;
    private Genre genre;
    private String description;
    private String author;
}
