package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Book;

import org.springframework.stereotype.Service;

@Service
public interface BookService {

    // 책 목록 조회
    public List<Book> listBook();

    // 카테고리에 해당하는 책 목록 조회
    public List<Book> listCategoryBook(Long categoryCode);

    // 내가 등록한 책 목록 조회
    public List<Book> listMyBook(String memberId);

    // 책 상세 조회
    public Book detailBook(Long no);

    // 책 수정
    public int updateBook(Book book);

    // 책 삭제
    public int deleteBook(Long no);

    // 책 등록
    public Book registerBook(Book book);
}
