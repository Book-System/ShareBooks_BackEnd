package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.BoardImage;

import org.springframework.stereotype.Service;

@Service
public interface BoardImageService {
    // 고객센터 글쓰기 시 이미지 등록
    public int insertBoardImage(BoardImage boardImage);

    // 고객센터 이미지 조회
    public List<BoardImage> selectBoardImagePreview(Long boardNo);

    // 고객센터 글삭제 시 이미지 삭제
    public int deleteBoardImage(Long boardNo);

    // 고객센터 글수정 시 이미지 수정
    public int updateBoardImage(BoardImage boardImage);

    // 고객센터 이미지 갯수 체크
    public int checkBoardImage(Long boardNo);

    // 이미지 번호 받아오기
    public BoardImage findBoardImage(Long boardimageNo);
}
