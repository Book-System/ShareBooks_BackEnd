package com.booksystem.service;

import java.util.Optional;

import com.booksystem.entity.BoardReply;
import com.booksystem.entity.BoardReplyProjection;
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
    public Optional<BoardReplyProjection> selectBoardReply(Long boardNo) {
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
}
