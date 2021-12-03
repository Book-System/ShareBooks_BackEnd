package com.booksystem.service;

import java.util.Optional;

import com.booksystem.entity.BoardReply;
import com.booksystem.entity.BoardReplyProjection;

import org.springframework.stereotype.Service;

@Service
public interface BoardReplyService {
    // 고객센터 답글 쓰기
    public int insertBoardReply(BoardReply boardReply);

    // 고객센터 답글 조회
    public Optional<BoardReplyProjection> selectBoardReply(Long boardNo);

    // 고객센터 답글 삭제
    public int deleteBoardReply(Long boardNo);

    // 고객센터 답글 수정
    public int updateBoardReply(BoardReply boardReply);
}
