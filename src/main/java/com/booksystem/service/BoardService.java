package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Board;
import com.booksystem.entity.BoardProjection;

import org.springframework.stereotype.Service;

@Service
public interface BoardService {
    // 고객센터 글쓰기
    public Board insertBoard(Board board);

    // 고객센터 글목록
    public List<BoardProjection> selectBoard(String memberId);

    // 고객센터 글삭제
    public int deleteBoard(Long boardNo);

    // 고객센터 글수정
    public int updateBoard(Board board);

    // 고객센터 글수정 시 글목록 불러오기
    public List<BoardProjection> selectUpdateBoard(Long boardNo);
}
