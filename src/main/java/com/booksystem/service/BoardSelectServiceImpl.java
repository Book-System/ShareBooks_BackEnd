package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.BoardSelect;
import com.booksystem.repository.BoardSelectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardSelectServiceImpl implements BoardSelectService {

    @Autowired
    BoardSelectRepository bsRepository;

    @Override
    public List<BoardSelect> selectBoardView() {
        return bsRepository.querySelectBoardView();
    }

}
