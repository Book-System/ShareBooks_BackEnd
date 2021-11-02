package com.booksystem.service;

import java.util.Optional;

import com.booksystem.entity.Board;
import com.booksystem.repository.BoardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardRepository boRepository;

    @Override
    public Board insertBoard(Board board) {
        return boRepository.save(board); // board 리턴 받기위해 쿼리문 안쓰고 Jpa
    }

    @Override
    public Optional<Board> selectOneBoard(Long boardNo) {
        return boRepository.querySelectOneBoard(boardNo);
    }

    @Override
    public String selectContentBoard(Long boardNo) {
        return boRepository.querySelectContentBoard(boardNo); // 고객센터 상세페이지(내용만)
    }

    @Override
    public int deleteBoard(Long boardNo) {
        return boRepository.queryDeleteBoard(boardNo);
    }

    @Override
    public int updateBoard(Board board) {
        return boRepository.queryUpdateBoard(board);
    }
}
