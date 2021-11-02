package com.booksystem.service;

import com.booksystem.entity.BoardReply;
import com.booksystem.repository.BoardReplyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardReplyServiceImpl implements BoardReplyService {

    @Autowired
    BoardReplyRepository brRepository;

    @Override
    public int insertBoardReply(BoardReply boardReply) {
        return brRepository.queryInsertBoaradReply(boardReply);
    }

    @Override
    public String selectBoardReply(Long boardNo) {
        return brRepository.querySelectBoardReply(boardNo);
    }

    @Override
    public int deleteBoardReply(Long boardNo) {
        return brRepository.queryDeleteBoardReply(boardNo);
    }

    @Override
    public int updateBoardReply(BoardReply boardReply) {
        return brRepository.queryUpdateBoardReply(boardReply);
    }

    @Override
    public int checkBoardReply(Long boardNo) {
        return brRepository.queryCheckBoardReply(boardNo);
    }
}
