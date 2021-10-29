package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Book;
import com.booksystem.entity.BookProjection;
import com.booksystem.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bRepository;

    @Override
    public List<BookProjection> listBook() {
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
    public Book detailBook(Long bookNo) {
        return bRepository.queryDetailBook(bookNo);
    }

    @Override
    public Book updateBook(Book book) {
        return bRepository.save(book);
    }

    @Override
    public int deleteBook(Long bookNo) {
        return bRepository.queryDeleteBook(bookNo);
    }

    @Override
    public Book registerBook(Book book) {
        return bRepository.save(book);
    }

    @Override
    public Book detailBookJPA(Long bookNo) {
        return bRepository.findByBookNo(bookNo);
    }
}