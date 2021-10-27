package com.booksystem.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.booksystem.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 고객센터 글목록
    @Query(value = "SELECT * FROM BOARD ORDER BY NO DESC", nativeQuery = true)
    public List<Board> querySelectBoard();

    // 고객센터 상세페이지
    @Query(value = "SELECT * FROM BOARD INNER JOIN BOARDIMAGE ON BOARD.NO = BOARDIMAGE.BOARD_NO AND BOARD.NO=:no", nativeQuery = true)
    public Optional<Board> querySeleItemOneBoard(@Param("no") Long no);

    // 고객센터 글삭제
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM Board WHERE NO=:no", nativeQuery = true)
    public int queryDeleteBoard(@Param("no") Long no);

    // 고객센터 글수정
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Board SET TITLE=:#{#board.title}, CONTENT=:#{#board.content}, INQUIRY=:#{#board.inquiry} WHERE NO=:#{#board.no}", nativeQuery = true)
    public int queryUpdateBoard(@Param("board") Board board);
}