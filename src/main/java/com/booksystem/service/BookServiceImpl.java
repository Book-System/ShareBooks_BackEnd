package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Book;
import com.booksystem.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bRepository;

    @Override
    public List<Book> listBook() {
        return bRepository.queryListBook();
    }

    @Override
    public List<Book> listCategoryBook(Long categoryCode) {
        return bRepository.queryListCategoryBook(categoryCode);
    }

    @Override
    public List<Book> listMyBook(String memberId) {
        return bRepository.queryListMyBook(memberId);
    }

    @Override
    public Book detailBook(Long no) {
        return bRepository.queryDetailBook(no);
    }

    @Override
    public int updateBook(Book book) {
        return bRepository.queryUpdateBook(book);
    }

    @Override
    public int deleteBook(Long no) {
        return bRepository.queryDeleteBook(no);
    }

    @Override
    public Book registerBook(Book book) {
        return bRepository.save(book);
    }
}
