package com.booksystem.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

import com.booksystem.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 카테고리 등록
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO CATEGORY(CODE, NAME) VALUES(SEQ_CATEGORY_NO.NEXTVAL, :#{#category.name})", nativeQuery = true)
    public int queryRegisterCategory(@Param("category") Category category);

    // 카테고리 수정
    @Transactional
    @Modifying
    @Query(value = "UPDATE CATEGORY SET NAME=:#{#category.name} WHERE CODE=:#{#category.code}", nativeQuery = true)
    public int queryUpdateCategory(@Param("category") Category category);

    // 카테고리 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CATEGORY WHERE CODE=:categoryCode", nativeQuery = true)
    public int queryDeleteCategory(@Param("categoryCode") Long categoryCode);

    // 카테고리 목록 조회
    @Query(value = "SELECT * FROM CATEGORY", nativeQuery = true)
    public List<Category> queryListCategory();

    // 카테고리 상세 조회
    @Query(value = "SELECT * FROM CATEGORY WHERE CODE=:categoryCode", nativeQuery = true)
    public Category queryDetailCategory(@Param("categoryCode") Long categoryCode);
}
