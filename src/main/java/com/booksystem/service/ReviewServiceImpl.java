package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Review;
import com.booksystem.entity.ReviewProjection;
import com.booksystem.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Override
    public int registerReview(Review review) {
        reviewRepository.save(review);
        return 1;
    }

    @Override
    public int removeReview(Long reviewNo) {
        return reviewRepository.queryRemoveReview(reviewNo);
    }

    @Override
    public ReviewProjection detailReview(Long reviewNo) {
        return reviewRepository.queryDetailReview(reviewNo);
    }

    @Override
    public int updateReview(Review review) {
        return reviewRepository.queryUpdateReview(review);
    }

    @Override
    public List<ReviewProjection> listReview(String memberId) {
        return reviewRepository.queryListReview(memberId);
    }

    @Override
    public int countReview(String memberId, Long bookNo) {
        return reviewRepository.queryCountReview(memberId, bookNo);
    }

    @Override
    public ReviewProjection avgReview(Long bookNo) {
        return reviewRepository.queryAvgReview(bookNo);
    }

    @Override
    public List<Review> bookNoReview(Long bookNo) {
        return reviewRepository.queryBookNoReview(bookNo);
    }

    @Override
    public Review reviewGet(Long reviewNo) {
        return reviewRepository.queryReviewGet(reviewNo);
    }
<<<<<<< Updated upstream
=======

    @Override
    public int countMyReview(String memberId) {
        return reviewRepository.queryCountMyReview(memberId);
    }

>>>>>>> Stashed changes
}