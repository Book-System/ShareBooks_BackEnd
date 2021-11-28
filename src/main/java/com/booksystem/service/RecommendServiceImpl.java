package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.RecommendBook;
import com.booksystem.entity.RecommendBookProjection;
import com.booksystem.repository.RecommendRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    RecommendRepository recommendRepository;

    @Override
    public List<RecommendBook> categoryRecommendBooks(Long categoryCode) {
        return recommendRepository.queryCategoryRecommendBooks(categoryCode);
    }

    @Override
    public List<RecommendBook> allRecommendBooks() {
        return recommendRepository.queryAllRecommendBooks();
    }

    @Override
    public List<RecommendBookProjection> recommendRatingBooks() {
        return recommendRepository.queryRecommendRatingBooks();
    }
}
