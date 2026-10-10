package com.snhu.mscs.cs600.module2;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Tyler Daniels
 * Exposes database-backed book records through the API.
 */
@RestController
@RequestMapping("/api/v1")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/database/books")
    public List<Book> getBooks() {
        return bookService.findAll();
    }
}