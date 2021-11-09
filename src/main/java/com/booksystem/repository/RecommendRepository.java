package com.booksystem.repository;

import java.util.List;

import com.booksystem.entity.RecommendBook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends JpaRepository<RecommendBook, Long> {

    // 카테고리별 이달의 도서 5권 추천
    @Query(value = "SELECT * FROM RECOMMEND_BOOK_VIEW WHERE CATEGORY_CODE=:categoryCode AND BORROW >= 2 ORDER BY(RATING) DESC LIMIT 5", nativeQuery = true)
    public List<RecommendBook> queryCategoryRecommendBooks(@Param("categoryCode") Long categoryCode);

    // 전체 이달의 도서 5권 추천
    @Query(value = "SELECT * FROM RECOMMEND_BOOK_VIEW WHERE BORROW >= 2 ORDER BY(RATING) DESC LIMIT 5", nativeQuery = true)
    public List<RecommendBook> queryAllRecommendBooks();
}