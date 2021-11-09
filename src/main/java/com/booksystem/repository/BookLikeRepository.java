package com.booksystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.booksystem.entity.Book;
import com.booksystem.entity.BookLike;
import com.booksystem.entity.BookLikeProjection;
import com.booksystem.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookLikeRepository extends JpaRepository<BookLike, Long> {

    // 물품 찜 등록
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO BOOKLIKE(BOOKLIKE_NO, BOOK_NO, MEMBER_ID) VALUES(SEQ_BOOKLIKE_NO.NEXTVAL, :#{#bookLike.book}, :#{#bookLike.member})", nativeQuery = true)
    public int queryRegisterBookLike(@Param("bookLike") BookLike bookLike);

    // 물품 찜 해제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BOOKLIKE WHERE BOOKLIKE_NO=:booklikeNo", nativeQuery = true)
    public int queryRemoveBookLike(@Param("booklikeNo") Long booklikeNo);

    // 개선된 물품 찜 해제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BOOKLIKE WHERE BOOK_NO=:book AND MEMBER_ID=:member", nativeQuery = true)
    public int queryRemoveBookLike2(@Param("book") Book book, @Param("member") Member member);

    // 물품 찜 개수 조회
    @Query(value = "SELECT COUNT(*) FROM BOOKLIKE WHERE MEMBER_ID=:member AND BOOK_NO=:book GROUP BY(BOOK_NO)", nativeQuery = true)
    public Object queryCountBookLike(@Param("member") Member member, @Param("book") Book book);

    // 물품 찜 목록 조회
    @Query(value = "SELECT * FROM BOOKLIKE WHERE MEMBER_ID=:member", nativeQuery = true)
    public List<BookLikeProjection> queryListBookLike(@Param("member") Member member);

    // 물품 찜 상세 조회
    @Query(value = "SELECT * FROM BOOKLIKE WHERE MEMBER_ID=:member AND BOOK_NO=:book", nativeQuery = true)
    public BookLikeProjection queryDetailBookLike(@Param("member") Member member, @Param("book") Book book);
}