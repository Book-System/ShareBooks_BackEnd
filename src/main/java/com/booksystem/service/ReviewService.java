package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Review;
import com.booksystem.entity.ReviewProjection;

import org.springframework.stereotype.Service;

@Service
public interface ReviewService {
    // 리뷰 등록
    public int registerReview(Review review);

    // 리뷰 삭제
    public int removeReview(Long reviewNo);

    // 리뷰 조회
    public ReviewProjection detailReview(Long reviewNo);

    // 리뷰 수정
    public int updateReview(Review review);

    // 리뷰 목록 조회
    public List<ReviewProjection> listReview(String memberId);

    // 평균 리뷰 점수, 리뷰 목록 조회
    public ReviewProjection avgReview(Long bookNo);

    // 책 번호에 해당하는 리뷰 목록 조회
    public List<Review> bookNoReview(Long bookNo);

    // 리뷰 개수 조회
    public int countReview(String memberId, Long bookNo);

    // 리뷰 객체 조회
    public Review reviewGet(Long reviewNo);

    // 리뷰 개수 조회(자신이 작성한 리뷰 개수 조회)
    public int countMyReview(String memberId);
}
