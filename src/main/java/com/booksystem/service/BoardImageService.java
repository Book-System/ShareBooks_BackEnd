package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.BoardImage;
import com.booksystem.entity.BoardImageProjection;

import org.springframework.stereotype.Service;

@Service
public interface BoardImageService {
    // 고객센터 글쓰기 시 이미지 등록
    public int insertBoardImage(BoardImage boardImage);

    // 고객센터 상세페이지 이미지 추출
    public List<BoardImageProjection> selectBoardImage(Long no);

    // 고객센터 이미지 조회
    public List<BoardImage> selectBoardImagePreview(Long no);

    // 고객센터 글삭제 시 이미지 삭제
    public int deleteBoardImage(Long no);

    // 고객센터 글수정 시 이미지 수정
    public int updateBoardImage(BoardImage boardImage);
}
