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

<<<<<<< Updated upstream
    // 평점 높은 책 8권 추천
    @Query(value = "SELECT R.BOOK_NO, R.RATING, B.TITLE, B.ADDRESS, B.PRICE, B.TAG, B.CATEGORY_CODE FROM (SELECT BOOK_NO, AVG(RATING) AS RATING FROM REVIEW GROUP BY(BOOK_NO)) AS R INNER JOIN BOOK AS B ON R.BOOK_NO=B.BOOK_NO ORDER BY(RATING) LIMIT 8;", nativeQuery = true)
    public List<RecommendBookProjection> queryRecommendRatingBooks();
=======
    // 평점 높은 책 10권 추천
    @Query(value = "SELECT R.BOOK_NO, R.RATING, B.TITLE, B.ADDRESS, B.PRICE, B.TAG, B.CATEGORY_CODE FROM (SELECT BOOK_NO, AVG(RATING) AS RATING FROM REVIEW GROUP BY(BOOK_NO)) AS R INNER JOIN BOOK AS B ON R.BOOK_NO=B.BOOK_NO ORDER BY(RATING) LIMIT 10;", nativeQuery = true)
    public List<RecommendBookProjection> queryRecommendRatingBooks();

    // 최신 리뷰 4개 조회
    @Query(value = "SELECT B.BOOK_NO, B.ADDRESS, B.BOOK_TITLE, B.TAG, C.NAME AS CNAME, R.CONTENT, M.NAME AS MNAME FROM BOOK B INNER JOIN CATEGORY C ON B.CATEGORY_CODE=C.CODE INNER JOIN REVIEW R ON B.BOOK_NO=R.BOOK_NO INNER JOIN MEMBER M ON B.MEMBER_ID=M.ID ORDER BY R.REGDATE DESC LIMIT 4;", nativeQuery = true)
    public List<RecommendBookProjection> queryCommentBooks();

    // 이달의 책 6권 조회
    @Query(value = "SELECT B.BOOK_NO, B.BOOK_TITLE,B.TAG, B.PRICE, R.COUNT AS RCOUNT FROM BOOK B INNER JOIN (SELECT BOOK_NO, TO_CHAR(REGDATE, 'YYYYMM') AS REGDATE, COUNT(*) AS COUNT FROM RESERVATION WHERE PAY_SUCCESS=TRUE AND TO_CHAR(REGDATE, 'YYYYMM')=TO_CHAR(SYSDATE, 'YYYYMM') GROUP BY(BOOK_NO, REGDATE)) R ON B.BOOK_NO=R.BOOK_NO ORDER BY RCOUNT DESC LIMIT 6;", nativeQuery = true)
    public List<RecommendBookProjection> queryMonthBooks();
>>>>>>> Stashed changes
}