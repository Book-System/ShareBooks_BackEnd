package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.BoardSelect;

import org.springframework.stereotype.Service;

@Service
public interface BoardSelectService {
    // 고객센터 글목록
    public List<BoardSelect> selectBoardView();
}
