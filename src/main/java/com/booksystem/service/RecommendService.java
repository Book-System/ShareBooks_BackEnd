package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.RecommendBook;
import com.booksystem.entity.RecommendBookProjection;

import org.springframework.stereotype.Service;

@Service
public interface RecommendService {

    // 이달의 책 5권 조회(카테고리 별로)
    public List<RecommendBook> categoryRecommendBooks(Long categoryCode);

    // 이달의 책 5권 조회(전체)
    public List<RecommendBook> allRecommendBooks();

    // 평점이 높은 책 20권 추천
    public List<RecommendBookProjection> recommendRatingBooks();

    // 최신 리뷰 4개 조회
    public List<RecommendBookProjection> commentBooks();

    // 이달의 인기 책 6권 조회
    public List<RecommendBookProjection> monthBooks();
}