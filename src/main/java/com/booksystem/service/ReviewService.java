package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Review;

import org.springframework.stereotype.Service;

@Service
public interface ReviewService {
    // 리뷰 등록
    public int registerReview(Review review);

    // 리뷰 삭제
    public int removeReview(Long reviewNo);

    // 리뷰 조회
    public Review detailReview(Long reviewNo);

    // 리뷰 수정
    public int updateReview(Review review);

    // 리뷰 목록 조회
    public List<Review> listReview();

    // 리뷰 개수 조회
    public int countReview(String memberId);
}