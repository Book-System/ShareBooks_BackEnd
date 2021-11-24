package com.booksystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.booksystem.entity.BookImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {

    // 책 이미지 등록
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO BOOKIMAGE(BOOKIMAGE_NO, IMAGE, IMAGESIZE, IMAGENAME, IMAGETYPE, PRIORITY, BOOK_NO) VALUES(SEQ_BOOKIMAGE_NO.NEXTVAL, :#{#bookImage.image}, :#{#bookImage.imagesize},:#{#bookImage.imagename}, :#{#bookImage.imagetype}, :#{#bookImage.priority}, :#{#bookImage.book})", nativeQuery = true)
    public int queryRegisterBookImage(@Param("bookImage") BookImage bookImage);

    // 책 이미지 수정
    @Transactional
    @Modifying
    @Query(value = "UPDATE BOOKIMAGE SET IMAGE=:#{#bookImage.image}, IMAGESIZE=:#{#bookImage.imagesize}, IMAGENAME=:#{#bookImage.imagename}, IMAGETYPE=:#{#bookImage.imagetype} WHERE BOOKIMAGE_NO=:#{#bookImage.bookimageNo}", nativeQuery = true)
    public int queryUpdateBookImage(@Param("bookImage") BookImage bookImage);

    // 책 이미지 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BOOKIMAGE WHERE BOOK_NO=:bookNo", nativeQuery = true)
    public int queryDeleteBookImage(@Param("bookNo") Long bookNo);

    // 책 이미지 목록 조회
    @Query(value = "SELECT * FROM BOOKIMAGE WHERE BOOK_NO=:bookNo", nativeQuery = true)
    public List<BookImage> queryListBookImage(@Param("bookNo") Long bookNo);

    // 책 메인 이미지 조회
    @Query(value = "SELECT * FROM BOOKIMAGE WHERE BOOK_NO=:bookNo AND PRIORITY=:priority", nativeQuery = true)
    public BookImage queryMainBookImage(@Param("bookNo") Long bookNo, @Param("priority") int priority);

    // 책 이미지 개수 조회
    @Query(value = "SELECT COUNT(*) FROM BOOKIMAGE WHERE BOOK_NO=:bookNo GROUP BY(BOOK_NO)", nativeQuery = true)
    public int queryCountBookImage(@Param("bookNo") Long bookNo);
}