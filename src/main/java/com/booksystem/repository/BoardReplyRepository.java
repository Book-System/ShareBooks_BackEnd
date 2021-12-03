package com.booksystem.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import com.booksystem.entity.BoardReply;
import com.booksystem.entity.BoardReplyProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {
    // 고객센터 답글 쓰기
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO BOARDREPLY(BOARDREPLY_NO, CONTENT, BOARD_NO) VALUES(SEQ_BOARDREPLY_NO.NEXTVAL, :#{#boardReply.content}, :#{#boardReply.board})", nativeQuery = true)
    public int queryInsertBoaradReply(@Param("boardReply") BoardReply boardReply);

    // 고객센터 답글 조회
    @Query(value = "SELECT * FROM BOARDREPLY WHERE BOARD_NO=:boardNo", nativeQuery = true)
    public Optional<BoardReplyProjection> querySelectBoardReply(@Param("boardNo") Long boardNo);

    // 고객센터 답글 삭제
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM BOARDREPLY WHERE BOARD_NO=:boardNo", nativeQuery = true)
    public int queryDeleteBoardReply(@Param("boardNo") Long boardNo);

    // 고객센터 답글 수정
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE BOARDREPLY SET CONTENT=:#{#boardReply.content} WHERE BOARDREPLY_NO=:#{#boardReply.boardreplyNo}", nativeQuery = true)
    public int queryUpdateBoardReply(@Param("boardReply") BoardReply boardReply);
}
