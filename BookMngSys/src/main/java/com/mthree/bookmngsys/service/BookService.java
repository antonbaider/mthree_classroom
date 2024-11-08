package com.mthree.bookmngsys.service;

import com.mthree.bookmngsys.model.Book;
import com.mthree.bookmngsys.repository.BookRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Getter
@AllArgsConstructor
public class BookService {

    @Autowired
    BookRepositoryImpl bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public Book getBooksById(int id) {
        return bookRepository.getBookById(id);
    }

    public void deleteBookById(int id) {
        bookRepository.deleteBooksById(id);
    }

    public void updateBooksById(int id, Book updatedBook) {
        bookRepository.updateBooksById(id, updatedBook);
    }

    public Book addBook(Book book) {
        return bookRepository.addBook(book);
    }
}