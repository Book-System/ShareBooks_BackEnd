package com.booksystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.booksystem.entity.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // 책 목록 조회
    @Query(value = "SELECT * FROM BOOK", nativeQuery = true)
    public List<Book> queryListBook();

    // 카테고리에 해당하는 책 목록 조회
    @Query(value = "SELECT * FROM BOOK WHERE CATEGORY_CODE=:categoryCode", nativeQuery = true)
    public List<Book> queryListCategoryBook(@Param("categoryCode") Long categoryCode);

    // 내가 등록한 책 목록 조회
    @Query(value = "SELECT * FROM BOOK WHERE MEMBER_ID=:memberId", nativeQuery = true)
    public List<Book> queryListMyBook(@Param("memberId") String memberId);

    // 책 상세 조회
    @Query(value = "SELECT * FROM BOOK WHERE NO=:no", nativeQuery = true)
    public Book queryDetailBook(@Param("no") Long no);

    // 책 수정
    @Transactional
    @Modifying
    @Query(value = "UPDATE BOOK SET TITLE=:#{#book.title}, CONTENT=:#{#book.content}, PRICE=:#{#book.price}, ADDRESS=:#{#book.address}, TAG=:#{#book.tag}, BOOK_TITLE=:#{#book.book_title}, BOOK_CONTENT=:#{#book.book_content} WHERE NO=:#{#book.no}", nativeQuery = true)
    public int queryUpdateBook(@Param("book") Book book);

    // 책 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BOOK WHERE NO=:no", nativeQuery = true)
    public int queryDeleteBook(@Param("no") Long no);
}
