package com.booksystem.service;

import java.util.List;
import java.util.Optional;

import com.booksystem.dto.SnsDTO;
import com.booksystem.mappers.SnsMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnsServiceImpl implements SnsService {

    @Autowired
    SnsMapper sMapper;

    @Override
    public int insertSns(SnsDTO sns) {
        return sMapper.queryInsertSns(sns);
    }

    @Override
    public List<SnsDTO> selectSnsAll(String memberId) {
        return sMapper.querySelectSnsAll(memberId);
    }

    @Override
    public Optional<SnsDTO> selectSns(Long snsNo) {
        return sMapper.querySelectSns(snsNo);
    }

    @Override
    public int deleteSns(Long snsNo) {
        return sMapper.queryDeleteSns(snsNo);
    }

    @Override
    public int updateSns(SnsDTO sns) {
        return sMapper.queryUpdateSns(sns);
    }

    @Override
    public List<SnsDTO> selectSnsGood(String memberId) {
        return sMapper.querySelectSnsGood(memberId);
    }
}
