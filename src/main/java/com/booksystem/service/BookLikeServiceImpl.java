package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Book;
import com.booksystem.entity.BookLike;
import com.booksystem.entity.BookLikeProjection;
import com.booksystem.entity.Member;
import com.booksystem.repository.BookLikeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookLikeServiceImpl implements BookLikeService {

    @Autowired
    BookLikeRepository bookLikeRepository;

    @Override
    public int registerBookLike(BookLike bookLike) {
        return bookLikeRepository.queryRegisterBookLike(bookLike);
    }

    @Override
    public int removeBookLike(Long booklikeNo) {
        return bookLikeRepository.queryRemoveBookLike(booklikeNo);
    }

    @Override
    public Object countBookLike(Member member, Book book) {
        return bookLikeRepository.queryCountBookLike(member, book);
    }

    @Override
    public List<BookLikeProjection> listBookLike(Member member) {
        return bookLikeRepository.queryListBookLike(member);
    }

    @Override
    public BookLikeProjection detailBookLike(Member member, Book book) {
        return bookLikeRepository.queryDetailBookLike(member, book);
    }

    @Override
    public int removeBookLike2(Book book, Member member) {
        return bookLikeRepository.queryRemoveBookLike2(book, member);
    }

}
