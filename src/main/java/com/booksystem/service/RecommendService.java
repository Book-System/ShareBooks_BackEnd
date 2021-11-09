package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.RecommendBook;

import org.springframework.stereotype.Service;

@Service
public interface RecommendService {

    // 이달의 책 5권 조회(카테고리 별로)
    public List<RecommendBook> categoryRecommendBooks(Long categoryCode);

    // 이달의 책 5권 조회(전체)
    public List<RecommendBook> allRecommendBooks();
}