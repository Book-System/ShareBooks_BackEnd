package com.booksystem.repository;

import java.util.List;
import javax.transaction.Transactional;

import com.booksystem.entity.BoardImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    // 고객센터 글쓰기 시 이미지 등록
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO BOARDIMAGE(BOARDIMAGE_NO, IMAGE, IMAGENAME, IMAGESIZE, IMAGETYPE, PRIORITY, BOARD_NO) VALUES(SEQ_BOARDIMAGE_NO.NEXTVAL, :#{#boardImage.image}, :#{#boardImage.imagename}, :#{#boardImage.imagesize}, :#{#boardImage.imagetype}, :#{#boardImage.priority}, :#{#boardImage.board})", nativeQuery = true)
    public int queryInsertBoaradImage(@Param("boardImage") BoardImage boardImage);

    // 고객센터 이미지 조회
    List<BoardImage> findByBoard_BoardNo(Long boardNo);

    // 고객센터 글삭제 시 이미지 삭제
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM BOARDIMAGE WHERE BOARD_NO=:boardNo", nativeQuery = true)
    public int queryDeleteBoardImage(@Param("boardNo") Long boardNo);

    // 고객센터 글수정 시 이미지 수정
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE BOARDIMAGE SET IMAGE=:#{#boardImage.image}, IMAGENAME=:#{#boardImage.imagename}, IMAGESIZE=:#{#boardImage.imagesize}, IMAGETYPE=:#{#boardImage.imagetype} WHERE BOARDIMAGE_NO=:#{#boardImage.boardimageNo}", nativeQuery = true)
    public int queryUpdateBoardImage(@Param("boardImage") BoardImage boardImage);

    // 고객센터 이미지 갯수 체크
    @Query(value = "SELECT COUNT(*) FROM BOARDIMAGE WHERE BOARD_NO=:boardNo GROUP BY (BOARD_NO)", nativeQuery = true)
    public int queryCheckBoardImage(@Param("boardNo") Long boardNo);

    // 이미지 번호 받아오기
    @Query(value = "SELECT * FROM BOARDIMAGE WHERE BOARDIMAGE_NO = :boardimageNo", nativeQuery = true)
    public BoardImage queryfindBoardImage(@Param("boardimageNo") Long boardimageNo);
}