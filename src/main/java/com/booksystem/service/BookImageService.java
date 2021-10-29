package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.BookImage;

import org.springframework.stereotype.Service;

@Service
public interface BookImageService {

    // 책 이미지 등록
    public int registerBookImage(BookImage bookImage);

    // 책 이미지 삭제
    public int deleteBookImage(Long bookNo);

    // 책 이미지 수정
    public int updateBookImage(BookImage bookImage);

    // 책 이미지 조회
    public List<BookImage> listBookImage(Long bookNo);

    // 책 이미지 하나 조회
    public BookImage getBookImage(Long bookimageNo);

    // 책 이미지 개수 조회
    public int countBookImage(Long bookNo);
}