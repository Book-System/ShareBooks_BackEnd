package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Book;
import com.booksystem.entity.BookLike;
import com.booksystem.entity.BookLikeProjection;
import com.booksystem.entity.Member;

import org.springframework.stereotype.Service;

@Service
public interface BookLikeService {

    // 물품 찜 등록
    public int registerBookLike(BookLike bookLike);

    // 물품 찜 해제
    public int removeBookLike(Long booklikeNo);

    // 개선된 물품 찜 해제
    public int removeBookLike2(Book book, Member member);

    // 물품 찜 개수 조회
    public Object countBookLike(Member member, Book book);

    // 물품 찜 목록 조회
    public List<BookLikeProjection> listBookLike(Member member);

    // 물품 찜 상세 조회
    public BookLikeProjection detailBookLike(Member member, Book book);
}
