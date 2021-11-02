package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.BoardImage;
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
    public List<BoardImage> selectBoardImagePreview(Long boardNo) {
        return biRepository.findByBoard_BoardNo(boardNo);
    }

    @Override
    public int deleteBoardImage(Long boardNo) {
        return biRepository.queryDeleteBoardImage(boardNo);
    }

    @Override
    public int updateBoardImage(BoardImage boardImage) {
        return biRepository.queryUpdateBoardImage(boardImage);
    }

    @Override
    public int checkBoardImage(Long boardNo) {
        return biRepository.queryCheckBoardImage(boardNo);
    }

    @Override
    public BoardImage findBoardImage(Long boardimageNo) {
        return biRepository.queryfindBoardImage(boardimageNo);
    }
}