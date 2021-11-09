package com.booksystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.booksystem.entity.Review;
import com.booksystem.entity.ReviewProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 리뷰 등록
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO REVIEW(REVIEW_NO, RATING, CONTENT, REGDATE, MEMBER_ID, BOOK_NO) VALUES(SEQ_REVIEW_NO.NEXTVAL, :#{#review.rating}, :#{#review.content}, CURRENT_TIMESTAMP, :#{#review.member}, :#{#review.book})", nativeQuery = true)
    public int queryRegisterReview(@Param("review") Review review);

    // 리뷰 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM REVIEW WHERE REVIEW_NO=:reviewNo", nativeQuery = true)
    public int queryRemoveReview(@Param("reviewNo") Long reviewNo);

    // 리뷰 수정
    @Transactional
    @Modifying
    @Query(value = "UPDATE REVIEW SET RATING=:#{#review.rating}, CONTENT=:#{#review.content} WHERE REVIEW_NO=:#{#review.reviewNo}", nativeQuery = true)
    public int queryUpdateReview(@Param("review") Review review);

    // 리뷰 상세 조회
    @Query(value = "SELECT * FROM REVIEW WHERE REVIEW_NO=:reviewNo", nativeQuery = true)
    public ReviewProjection queryDetailReview(@Param("reviewNo") Long reviewNo);

    // 리뷰 목록 조회
    @Query(value = "SELECT * FROM REVIEW", nativeQuery = true)
    public List<ReviewProjection> queryListReview();

    // 리뷰 개수 조회
    @Query(value = "SELECT COUNT(*) FROM REVIEW WHERE MEMBER_ID=:memberId AND BOOK_NO=:bookNo", nativeQuery = true)
    public int queryCountReview(@Param("memberId") String memberId, @Param("bookNo") Long bookNo);
}