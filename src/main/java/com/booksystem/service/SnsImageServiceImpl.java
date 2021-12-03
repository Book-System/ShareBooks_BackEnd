package com.booksystem.service;

import java.util.List;

import com.booksystem.dto.SnsImageDTO;
import com.booksystem.mappers.SnsImageMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnsImageServiceImpl implements SnsImageService {

    @Autowired
    SnsImageMapper siMapper;

    @Override
    public int insertSnsImage(SnsImageDTO snsimage) {
        return siMapper.queryInsertImageSns(snsimage);
    }

    @Override
    public List<SnsImageDTO> selectSnsImage(Long snsNo) {
        return siMapper.querySelectSnsImage(snsNo);
    }

    @Override
    public int deleteImageSns(Long snsNo) {
        return siMapper.queryDeleteImageSns(snsNo);
    }

    @Override
    public int updateImageSns(SnsImageDTO snsimage) {
        return siMapper.queryupdateImageSns(snsimage);
    }

    @Override
    public SnsImageDTO findSnsImage(Long snsNo, int priority) {
        return siMapper.queryfindSnsImage(snsNo, priority);
    }

    @Override
    public List<SnsImageDTO> findSnsImagePriority(Long snsNo) {
        return siMapper.queryfindSnsImagePriority(snsNo);
    }

}
