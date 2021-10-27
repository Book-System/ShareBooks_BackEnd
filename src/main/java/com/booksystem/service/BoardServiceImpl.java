package com.booksystem.service;

import java.util.List;

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
    public List<Board> selectBoard() {
        return boRepository.querySelectBoard();
    }

    @Override
    public Board selectOneBoard(Long no) {
        return boRepository.querySeleItemOneBoard(no).orElse(null);
    }

    @Override
    public int deleteBoard(Long no) {
        return boRepository.queryDeleteBoard(no);
    }

    @Override
    public int updateBoard(Board board) {
        return boRepository.queryUpdateBoard(board);
    }

    // @Override
    // public Board getByBoardNo() {
    // return null;
    // }
}
