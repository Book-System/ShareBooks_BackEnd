package com.booksystem.service;

import java.util.List;

import com.booksystem.dto.SnsReplyDTO;

import org.springframework.stereotype.Service;

@Service
public interface SnsReplyService {
    // 우리동네 댓글 쓰기
    public int insertReplySns(SnsReplyDTO snsreply);

    // 우리동네 댓글 조회
    public List<SnsReplyDTO> selectReplySns(Long snsNo);

    // 우리동네 댓글 삭제
    public int deleteReplySns(Long snsreplyNo);

    // 우리동네 댓글 수정
    public int updateReplySns(SnsReplyDTO snsreply);

    // 우리동네 글삭제 시 댓글 삭제
    public int deleteRepliesSns(Long snsNo);
}
