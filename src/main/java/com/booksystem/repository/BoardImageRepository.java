package com.booksystem.repository;

import java.util.List;
import javax.transaction.Transactional;

import com.booksystem.entity.BoardImage;
import com.booksystem.entity.BoardImageProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    // 고객센터 글쓰기 시 이미지 등록
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO BOARDIMAGE(NO, IMAGE, IMAGENAME, IMAGESIZE, IMAGETYPE, PRIORITY, BOARD_NO) VALUES(SEQ_BOARDIMAGE_NO.NEXTVAL, :#{#boardImage.image}, :#{#boardImage.imagename}, :#{#boardImage.imagesize}, :#{#boardImage.imagetype}, :#{#boardImage.priority}, :#{#boardImage.board})", nativeQuery = true)
    public int queryInsertBoaradImage(@Param("boardImage") BoardImage boardImage);

    // 고객센터 상세페이지 이미지 추출
    @Query(value = "SELECT * FROM BOARDIMAGE WHERE BOARD_NO=:no", nativeQuery = true)
    public List<BoardImageProjection> querySelectBoaradImage(@Param("no") Long no);

    // 고객센터 이미지 조회
    List<BoardImage> findByBoard_No(Long no);

    // 고객센터 글삭제 시 이미지 삭제
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM BOARDIMAGE WHERE BOARD_NO=:no", nativeQuery = true)
    public int queryDeleteBoardImage(@Param("no") Long no);

    // 고객센터 글수정 시 이미지 수정
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE BoardImage SET IMAGE=:#{#boardImage.image}, IMAGENAME=:#{#boardImage.imagename}, IMAGESIZE=:#{#boardImage.imagesize}, IMAGETYPE=:#{#boardImage.imagetype} WHERE BOARD_NO=:#{#boardImage.no} AND PRIORITY=:#{#boardImage.priority}", nativeQuery = true)
    public int queryUpdateBoardImage(@Param("boardImage") BoardImage boardImage);

}
