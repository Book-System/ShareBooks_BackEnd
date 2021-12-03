package com.booksystem.service;

import java.util.List;

import com.booksystem.dto.SnsReplyDTO;
import com.booksystem.mappers.SnsReplyMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnsReplyServiceImpl implements SnsReplyService {

    @Autowired
    SnsReplyMapper srMapper;

    @Override
    public int insertReplySns(SnsReplyDTO snsreply) {
        return srMapper.queryInsertReplySns(snsreply);
    }

    @Override
    public List<SnsReplyDTO> selectReplySns(Long snsNo) {
        return srMapper.querySelectReplySns(snsNo);
    }

    @Override
    public int deleteReplySns(Long snsreplyNo) {
        return srMapper.queryDeleteReplySns(snsreplyNo);
    }

    @Override
    public int updateReplySns(SnsReplyDTO snsreply) {
        return srMapper.queryUpdateReplySns(snsreply);
    }

    @Override
    public int deleteRepliesSns(Long snsNo) {
        return srMapper.queryDeleteRepliesSns(snsNo);
    }

}
