package com.booksystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.booksystem.entity.Book;
import com.booksystem.entity.BookProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // 책 목록 조회
    @Query(value = "SELECT * FROM BOOK", nativeQuery = true)
    public List<BookProjection> queryListBook();

    // 내가 등록한 책 목록 조회
    @Query(value = "SELECT * FROM BOOK WHERE MEMBER_ID=:memberId", nativeQuery = true)
    public List<BookProjection> queryListRendBook(@Param("memberId") String memberId);

    // 카테고리에 해당하는 책 목록 조회
    @Query(value = "SELECT * FROM BOOK WHERE CATEGORY_CODE=:categoryCode", nativeQuery = true)
    public List<Book> queryListCategoryBook(@Param("categoryCode") Long categoryCode);

    // 책 상세 조회
    @Query(value = "SELECT * FROM BOOK WHERE BOOK_NO=:bookNo", nativeQuery = true)
    public Book queryDetailBook(@Param("bookNo") Long bookNo);

    // 책 상세 조회(원하는 항목만)
    @Query(value = "SELECT * FROM BOOK WHERE BOOK_NO=:bookNo", nativeQuery = true)
    public BookProjection queryDetailBookProjection(@Param("bookNo") Long bookNo);

    // 책 상세 조회(JPA)
    public Book findByBookNo(Long bookNo);

    // 책 수정
    @Transactional
    @Modifying
    @Query(value = "UPDATE BOOK SET TITLE=:#{#book.title}, CONTENT=:#{#book.content}, PRICE=:#{#book.price}, ADDRESS=:#{#book.address}, TAG=:#{#book.tag}, BOOK_TITLE=:#{#book.bookTitle}, BOOK_CONTENT=:#{#book.bookContent}, CATEGORY_CODE=:#{#book.category} WHERE BOOK_NO=:#{#book.bookNo}", nativeQuery = true)
    public int queryUpdateBook(@Param("book") Book book);

    // 책 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BOOK WHERE BOOK_NO=:bookNo", nativeQuery = true)
    public int queryDeleteBook(@Param("bookNo") Long bookNo);
}