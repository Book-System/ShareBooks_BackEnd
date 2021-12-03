package com.booksystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.booksystem.entity.Board;
import com.booksystem.entity.BoardProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 고객센터 글목록
    @Query(value = "SELECT BOARD_NO, CONTENT, INQUIRY, REGDATE, TITLE, RESERVATION_NO, MEMBER_ID FROM BOARD WHERE MEMBER_ID=:memberId", nativeQuery = true)
    public List<BoardProjection> querySelectBoard(@Param("memberId") String memberId);

    // 고객센터 글삭제
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM BOARD WHERE BOARD_NO=:boardNo", nativeQuery = true)
    public int queryDeleteBoard(@Param("boardNo") Long boardNo);

    // 고객센터 글수정
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE BOARD SET TITLE=:#{#board.title}, CONTENT=:#{#board.content}, INQUIRY=:#{#board.inquiry}, RESERVATION_NO=:#{#board.reservation} WHERE BOARD_NO=:#{#board.boardNo}", nativeQuery = true)
    public int queryUpdateBoard(@Param("board") Board board);

    // 고객센터 글수정 시 글목록 불러오기
    @Query(value = "SELECT BOARD_NO, CONTENT, INQUIRY, REGDATE, TITLE, RESERVATION_NO, MEMBER_ID FROM BOARD WHERE BOARD_NO=:boardNo", nativeQuery = true)
    public List<BoardProjection> querySelectUpdateBoard(@Param("boardNo") Long boardNo);
}