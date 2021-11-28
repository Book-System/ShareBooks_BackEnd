package com.booksystem.repository;

import java.util.List;

import com.booksystem.entity.RecommendBook;
import com.booksystem.entity.RecommendBookProjection;

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

    // 평점 높은 책 8권 추천
    @Query(value = "SELECT R.BOOK_NO, R.RATING, B.TITLE, B.ADDRESS, B.PRICE, B.TAG, B.CATEGORY_CODE FROM (SELECT BOOK_NO, AVG(RATING) AS RATING FROM REVIEW GROUP BY(BOOK_NO)) AS R INNER JOIN BOOK AS B ON R.BOOK_NO=B.BOOK_NO ORDER BY(RATING) LIMIT 8;", nativeQuery = true)
    public List<RecommendBookProjection> queryRecommendRatingBooks();
}