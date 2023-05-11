package com.example.springbatchexample.shell;

import com.example.springbatchexample.domain.Book;
import com.example.springbatchexample.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {
    private final BookService bookService;

    @ShellMethod(value = "Get all books", key = {"ab", "all-books"})
    public void getAllBooks() {
        List<Book> books = bookService.getAllBooks();

        books.stream()
                .map(this::getBookInfo)
                .forEach(System.out::println);
    }

    private String getBookInfo(Book book) {
        return book.getId() + ": " + book.getTitle() + ", " + book.getAuthor().getName() + " " +
                book.getAuthor().getSurname() + ", " + book.getGenre().getName();
    }

    @ShellMethod(value = "deleteAllBooks", key = "da")
    public void deleteAllBooks() {
        bookService.deleteAllBooks();
    }
}
