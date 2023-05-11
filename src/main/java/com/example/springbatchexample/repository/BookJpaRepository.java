package com.example.springbatchexample.repository;

import com.example.springbatchexample.domain.BookJpa;
import com.example.springbatchexample.dto.BookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookJpaRepository extends JpaRepository<BookJpa, Long> {

    @Query("select new com.example.springbatchexample.dto.BookResponseDto (" +
            "b.bookId, " +
            "b.bookName, " +
            "b.author.surname, " +
            "b.author.name, " +
            "b.genre.name) " +
            "from BookJpa b")
    Page<BookResponseDto> findAllInitialized(PageRequest pageRequest);
}
