package com.booksystem.service;

import com.booksystem.entity.BoardReply;

import org.springframework.stereotype.Service;

@Service
public interface BoardReplyService {
    // 고객센터 답글 쓰기
    public int insertBoardReply(BoardReply boardReply);

    // 고객센터 답글 조회
    public String selectBoardReply(Long boardNo);
    // public BoardReply selectBoardReply(Long no);

    // 고객센터 답글 삭제
    public int deleteBoardReply(Long boardNo);

    // 고객센터 답글 수정
    public int updateBoardReply(BoardReply boardReply);

    // 고객센터 답글 쓰기 전 갯수 체크
    public int checkBoardReply(Long boardNo);
}
