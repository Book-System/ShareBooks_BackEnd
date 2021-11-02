package com.booksystem.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.booksystem.entity.Board;
import com.booksystem.entity.ReservationProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 고객센터 상세페이지
    @Query(value = "SELECT * FROM BOARD INNER JOIN BOARDIMAGE ON BOARD.BOARD_NO = BOARDIMAGE.BOARD_NO AND BOARD.BOARD_NO=:boardNo", nativeQuery = true)
    public Optional<Board> querySelectOneBoard(@Param("boardNo") Long boardNo);

    // 고객센터 상세페이지(내용만)
    @Query(value = "SELECT Content FROM BOARD WHERE BOARD_NO=:boardNo", nativeQuery = true)
    public String querySelectContentBoard(@Param("boardNo") Long boardNo);

    // 고객센터 글삭제
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM BOARD WHERE BOARD_NO=:boardNo", nativeQuery = true)
    public int queryDeleteBoard(@Param("boardNo") Long boardNo);

    // 고객센터 글수정
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE BOARD SET TITLE=:#{#board.title}, CONTENT=:#{#board.content}, INQUIRY=:#{#board.inquiry} WHERE BOARD_NO=:#{#board.boardNo}", nativeQuery = true)
    public int queryUpdateBoard(@Param("board") Board board);

    // 고객센터 예약조회
    @Query(value = "SELECT RESERVATION_NO, RESERVATION_END_DATE, BOOK_NO FROM RESERVATION WHERE MEMBER_ID=:memberId", nativeQuery = true)
    public List<ReservationProjection> querySelectReservation(@Param("memberId") String memberId);

}