package com.booksystem.service;

import java.util.Optional;

import com.booksystem.entity.Board;

import org.springframework.stereotype.Service;

@Service
public interface BoardService {
    // 고객센터 글쓰기
    public Board insertBoard(Board board);

    // 고객센터 상세페이지
    public Optional<Board> selectOneBoard(Long boardNo);

    // 고객센터 상세페이지(내용만)
    public String selectContentBoard(Long boardNo);

    // 고객센터 글삭제
    public int deleteBoard(Long boardNo);

    // 고객센터 글수정
    public int updateBoard(Board board);
}
