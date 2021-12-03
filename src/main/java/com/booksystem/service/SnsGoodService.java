package com.booksystem.service;

import com.booksystem.dto.SnsGoodDTO;

import org.springframework.stereotype.Service;

@Service
public interface SnsGoodService {
    // 우리동네 좋아요(따봉)
    public int insertGoodSns(SnsGoodDTO snsgood);

    // 우리동네 좋아요취소(따봉삭제)
    public int deleteGoodSns(Long snsNo, String memberId);

    // 우리동네 좋아요 개수 체크(내가 했는지 안했는지)
    public int checkGoodSns(Long snsNo, String memberId);

    // 우리동네 글삭제 시 좋아요 삭제
    public int deleteGoodsSns(Long snsNo);
}
