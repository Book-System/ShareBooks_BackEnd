package com.booksystem.service;

import java.util.List;
import java.util.Optional;

import com.booksystem.dto.SnsDTO;

import org.springframework.stereotype.Service;

@Service
public interface SnsService {
    // 우리동네 글쓰기
    public int insertSns(SnsDTO sns);

    // 우리동네 글목록
    public List<SnsDTO> selectSnsAll(String memberId);

    // 우리동네 글수정 시 글목록
    public Optional<SnsDTO> selectSns(Long snsNo);

    // 우리동네 글삭제
    public int deleteSns(Long snsNo);

    // 우리동네 글수정
    public int updateSns(SnsDTO sns);

    // 우리동네 좋아요 목록
    public List<SnsDTO> selectSnsGood(String memberId);
}
