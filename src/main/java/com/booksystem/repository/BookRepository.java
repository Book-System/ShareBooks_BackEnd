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

    // 책 검색 목록 조회
    @Query(value = "SELECT * FROM (SELECT B.BOOK_NO, B.TITLE, B.PRICE, B.ADDRESS, B.TAG, B.BOOK_TITLE, B.REGDATE, M.ID AS MEMBER_ID, M.NAME AS MEMBER_NAME, M.NICKNAME AS MEMBER_NICKNAME, AVG(R.RATING) AS RATING, COUNT(R.*) AS COUNT, B.HIT, ROW_NUMBER() OVER (ORDER BY B.BOOK_NO DESC) ROWN FROM BOOK B INNER JOIN MEMBER M ON B.MEMBER_ID = M.ID LEFT OUTER JOIN REVIEW R ON B.BOOK_NO = R.BOOK_NO WHERE B.ADDRESS LIKE %:address% GROUP BY(B.BOOK_NO)) ADDRESS WHERE ROWN BETWEEN :page*4-3 AND :page*4", nativeQuery = true)
    public List<BookProjection> queryListSearchBook(@Param("address") String address, @Param("page") int page);

    // 책 개수 조회
    @Query(value = "SELECT COUNT(*) FROM BOOK WHERE MEMBER_ID=:memberId", nativeQuery = true)
    public int queryCountBook(String memberId);

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
    @Query(value = "SELECT B.BOOK_NO, B.ADDRESS, B.BOOK_CONTENT, B.TITLE, B.CONTENT, B.HIT, B.PRICE, B.REGDATE, B.TAG, B.BOOK_TITLE, M.ID AS MEMBER_ID, M.NAME AS MEMBER_NAME, M.NICKNAME AS MEMBER_NICKNAME, C.NAME AS CATEGORY_NAME FROM BOOK B INNER JOIN MEMBER M ON B.MEMBER_ID=M.ID INNER JOIN CATEGORY C ON B.CATEGORY_CODE=C.CODE WHERE BOOK_NO=:bookNo", nativeQuery = true)
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