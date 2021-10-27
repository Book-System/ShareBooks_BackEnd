package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Board;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {
    // 고객센터 글쓰기
    public Board insertBoard(Board board);

    // 고객센터 글목록
    public List<Board> selectBoard();

    // 고객센터 상세페이지
    public Board selectOneBoard(Long no);

    // 고객센터 글삭제
    public int deleteBoard(Long no);

    // 고객센터 글수정
    public int updateBoard(Board board);

    // // 고객센터 보드번호Get
    // public Board getByBoardNo();
}
