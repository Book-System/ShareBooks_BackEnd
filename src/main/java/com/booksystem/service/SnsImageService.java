package com.booksystem.service;

import java.util.List;

import com.booksystem.dto.SnsImageDTO;

import org.springframework.stereotype.Service;

@Service
public interface SnsImageService {
    // 우리동네 글쓰기 시 이미지 등록
    public int insertSnsImage(SnsImageDTO snsimage);

    // 우리동네 이미지 조회
    public List<SnsImageDTO> selectSnsImage(Long snsNo);

    // 우리동네 글삭제 시 이미지 삭제
    public int deleteImageSns(Long snsNo);

    // 우리동네 글수정 시 이미지 수정
    public int updateImageSns(SnsImageDTO snsimage);

    // 우리동네 글수정 시 이미지 수정 이미지 번호 받아오기
    public SnsImageDTO findSnsImage(Long snsNo, int priority);

    // 이미지 수정 시 이미지 정보 받아오기
    public List<SnsImageDTO> findSnsImagePriority(Long snsNo);
}
