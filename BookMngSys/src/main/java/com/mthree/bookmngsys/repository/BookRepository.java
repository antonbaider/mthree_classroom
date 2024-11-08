package com.mthree.bookmngsys.repository;

import com.mthree.bookmngsys.model.Book;

import java.util.List;

interface BookRepository {
    Book addBook(Book book);

    List<Book> getAllBooks();

    Book getBookById(int id);

    void updateBooksById(int id, Book updatedBook);

    void deleteBooksById(int id);
}
