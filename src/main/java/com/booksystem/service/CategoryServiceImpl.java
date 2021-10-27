package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Category;
import com.booksystem.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository cRepository;

    @Override
    public int registerCategory(Category category) {
        return cRepository.queryRegisterCategory(category);
    }

    @Override
    public int updateCategory(Category category) {
        return cRepository.queryUpdateCategory(category);
    }

    @Override
    public int deleteCategory(Long categoryCode) {
        return cRepository.queryDeleteCategory(categoryCode);
    }

    @Override
    public List<Category> listCategory() {
        return cRepository.queryListCategory();
    }

    @Override
    public Category detailCategory(Long categoryCode) {
        return cRepository.queryDetailCategory(categoryCode);
    }
}
