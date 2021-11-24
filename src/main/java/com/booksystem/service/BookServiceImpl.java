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
    BookRepository bookRepository;

    @Override
    public List<BookProjection> listBook() {
        return bookRepository.queryListBook();
    }

    @Override
    public List<BookProjection> listRendBook(String memberId) {
        return bookRepository.queryListRendBook(memberId);
    }

    @Override
    public List<Book> listCategoryBook(Long categoryCode) {
        return bookRepository.queryListCategoryBook(categoryCode);
    }

    @Override
    public Book detailBook(Long bookNo) {
        return bookRepository.queryDetailBook(bookNo);
    }

    @Override
    public Book detailBookJPA(Long bookNo) {
        return bookRepository.findByBookNo(bookNo);
    }

    @Override
    public BookProjection detailBookProjection(Long bookNo) {
        return bookRepository.queryDetailBookProjection(bookNo);
    }

    @Override
    public int updateBook(Book book) {
        return bookRepository.queryUpdateBook(book);
    }

    @Override
    public int deleteBook(Long bookNo) {
        return bookRepository.queryDeleteBook(bookNo);
    }

    @Override
    public Book registerBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public int countBook(String memberId) {
        return bookRepository.queryCountBook(memberId);
    }

    @Override
    public List<BookProjection> listSearchBook(String address, int page) {
        return bookRepository.queryListSearchBook(address, page);
    }
}