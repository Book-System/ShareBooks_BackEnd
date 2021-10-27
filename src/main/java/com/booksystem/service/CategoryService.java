package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Category;

import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    // 카테고리 등록
    public int registerCategory(Category category);

    // 카테고리 수정
    public int updateCategory(Category category);

    // 카테고리 삭제
    public int deleteCategory(Long categoryCode);

    // 카테고리 목록 조회
    public List<Category> listCategory();

    // 카테고리 상세 조회
    public Category detailCategory(Long categoryCode);
}
