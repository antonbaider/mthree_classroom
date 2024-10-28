package com.mthree.bookmngsys.controller;

import com.mthree.bookmngsys.Book;
import com.mthree.bookmngsys.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/books/{id}")
    public Book getBooksById(@PathVariable("id") int id) {
        return bookService.getBooksById(id);
    }
}
