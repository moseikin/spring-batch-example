package com.example.springbatchexample.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BookResponseDto {
    private final Long id;

    private final String title;

    private final String authorSurname;

    private final String authorName;

    private final String genreName;
}
