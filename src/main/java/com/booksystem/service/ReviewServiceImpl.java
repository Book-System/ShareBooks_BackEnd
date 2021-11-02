package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Review;
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
    public Review detailReview(Long reviewNo) {
        return reviewRepository.queryDetailReview(reviewNo);
    }

    @Override
    public int updateReview(Review review) {
        return reviewRepository.queryUpdateReview(review);
    }

    @Override
    public List<Review> listReview() {
        return reviewRepository.queryListReview();
    }

    @Override
    public int countReview(String memberId) {
        return reviewRepository.queryCountReview(memberId);
    }
}
