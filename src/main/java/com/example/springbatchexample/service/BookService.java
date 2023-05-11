package com.example.springbatchexample.service;

import com.example.springbatchexample.domain.Book;
import com.example.springbatchexample.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteAllBooks() {
        bookRepository.deleteAll();
    }
}
