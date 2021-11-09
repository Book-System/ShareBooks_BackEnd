package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.BookImage;
import com.booksystem.repository.BookImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookImageServiceImpl implements BookImageService {

    @Autowired
    BookImageRepository bookImageRepository;

    @Override
    public int registerBookImage(BookImage bookImage) {
        return bookImageRepository.queryRegisterBookImage(bookImage);
    }

    @Override
    public int deleteBookImage(Long bookNo) {
        return bookImageRepository.queryDeleteBookImage(bookNo);
    }

    @Override
    public int updateBookImage(BookImage bookImage) {
        return bookImageRepository.queryUpdateBookImage(bookImage);
    }

    @Override
    public List<BookImage> listBookImage(Long bookNo) {
        return bookImageRepository.queryListBookImage(bookNo);
    }

    @Override
    public BookImage getBookImage(Long bookimageNo) {
        return bookImageRepository.getById(bookimageNo);
    }

    @Override
    public int countBookImage(Long bookNo) {
        return bookImageRepository.queryCountBookImage(bookNo);
    }
}