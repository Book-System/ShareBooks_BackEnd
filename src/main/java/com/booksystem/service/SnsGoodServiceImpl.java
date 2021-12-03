package com.booksystem.service;

import com.booksystem.dto.SnsGoodDTO;
import com.booksystem.mappers.SnsGoodMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnsGoodServiceImpl implements SnsGoodService {

    @Autowired
    SnsGoodMapper sgMapper;

    @Override
    public int insertGoodSns(SnsGoodDTO snsgood) {
        return sgMapper.queryInsertGoodSns(snsgood);
    }

    @Override
    public int deleteGoodSns(Long snsNo, String memberId) {
        return sgMapper.queryDeleteGoodSns(snsNo, memberId);
    }

    @Override
    public int checkGoodSns(Long snsNo, String memberId) {
        return sgMapper.queryCheckGoodSns(snsNo, memberId);
    }

    @Override
    public int deleteGoodsSns(Long snsNo) {
        return sgMapper.queryDeleteGoodsSns(snsNo);
    }

}
