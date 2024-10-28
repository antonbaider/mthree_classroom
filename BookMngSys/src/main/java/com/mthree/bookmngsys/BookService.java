package com.mthree.bookmngsys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Setter
@Getter
@AllArgsConstructor
public class BookService {
    private static List<Book> myBookList = new ArrayList<>();

    static {
        myBookList.add(new Book(1, "Book 1", "Author 1"));

        myBookList.add(new Book(2, "Book 2", "Author 2"));

        myBookList.add(new Book(3, "Book 3", "Author 3"));
    }

    public List<Book> getAllBooks() {
        return myBookList;
    }

    public Book getBooksById(int id) {
        Book book;
        book = myBookList.stream().filter(b -> b.getId() == id).findFirst().get();
        return book;
    }
}
