package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.BoardImage;
import com.booksystem.entity.BoardImageProjection;
import com.booksystem.repository.BoardImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardImageServiceImpl implements BoardImageService {

    @Autowired
    BoardImageRepository biRepository;

    @Override
    public int insertBoardImage(BoardImage boardImage) {
        return biRepository.queryInsertBoaradImage(boardImage);
    }

    @Override
    public List<BoardImageProjection> selectBoardImage(Long no) {
        return biRepository.querySelectBoaradImage(no);
    }

    @Override
    public List<BoardImage> selectBoardImagePreview(Long no) {
        return biRepository.findByBoard_No(no);
    }

    @Override
    public int deleteBoardImage(Long no) {
        return biRepository.queryDeleteBoardImage(no);
    }

    @Override
    public int updateBoardImage(BoardImage boardImage) {
        return biRepository.queryUpdateBoardImage(boardImage);
    }
}