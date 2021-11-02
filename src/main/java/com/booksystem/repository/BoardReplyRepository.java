package com.booksystem.repository;

import javax.transaction.Transactional;

import com.booksystem.entity.BoardReply;

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
    @Query(value = "INSERT INTO BOARDREPLY(BOARDREPLY_NO, CONTENT, BOARD_NO, MEMBER_ID) VALUES(SEQ_BOARDREPLY_NO.NEXTVAL, :#{#boardReply.content}, :#{#boardReply.board.boardNo}, :#{#boardReply.member.id})", nativeQuery = true)
    public int queryInsertBoaradReply(@Param("boardReply") BoardReply boardReply);

    // 고객센터 답글 조회
    @Query(value = "SELECT CONTENT FROM BOARDREPLY WHERE BOARD_NO=:boardNo", nativeQuery = true)
    public String querySelectBoardReply(@Param("boardNo") Long boardNo);
    // public Optional<BoardReply> querySelectBoardReply(@Param("no") Long no);

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

    // 고객센터 답글 쓰기 전 갯수 체크
    @Query(value = "SELECT COUNT(*) FROM BOARDREPLY WHERE BOARD_NO=:boardNo GROUP BY (BOARD_NO)", nativeQuery = true)
    public int queryCheckBoardReply(@Param("boardNo") Long boardNo);
}
