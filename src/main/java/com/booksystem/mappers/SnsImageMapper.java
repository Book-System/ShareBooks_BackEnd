package com.booksystem.mappers;

import java.util.List;

import com.booksystem.dto.SnsImageDTO;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SnsImageMapper {
        // 우리동네 글쓰기 시 이미지 등록
        @Insert({ "INSERT INTO SNSIMAGE(SNSIMAGE_NO, IMAGE, IMAGENAME, IMAGESIZE, IMAGETYPE, PRIORITY, SNS_NO) ",
                        "VALUES (SEQ_SNSIMAGE_NO.NEXTVAL, #{image}, #{imagename}, #{imagesize}, #{imagetype}, #{priority}, #{snsNo})" })
        public int queryInsertImageSns(SnsImageDTO snsimage);

        // 우리동네 이미지 조회
        @Select({ "SELECT * FROM SNSIMAGE WHERE SNS_NO=#{snsNo}" })
        @Results({ @Result(property = "snsimageNo", column = "SNSIMAGE_NO"),
                        @Result(property = "snsNo", column = "SNS_NO") })
        public List<SnsImageDTO> querySelectSnsImage(Long snsNo);

        // 우리동네 글삭제 시 이미지 삭제
        @Delete({ "DELETE FROM SNSIMAGE WHERE SNS_NO=#{snsNo}" })
        public int queryDeleteImageSns(Long snsNo);

        // 우리동네 글수정 시 이미지 수정
        @Update({
                        "UPDATE SNSIMAGE SET IMAGE=#{image}, IMAGENAME=#{imagename}, IMAGESIZE=#{imagesize}, IMAGETYPE=#{imagetype} WHERE SNSIMAGE_NO=#{snsimageNo}" })
        @Results({ @Result(property = "snsimageNo", column = "SNSIMAGE_NO"),
                        @Result(property = "snsNo", column = "SNS_NO") })
        public int queryupdateImageSns(SnsImageDTO snsimage);

        // 우리동네 글수정 시 이미지 수정 이미지 정보 받아오기
        @Select({ "SELECT * FROM SNSIMAGE WHERE SNS_NO = #{snsNo} AND PRIORITY = #{priority}" })
        @Results({ @Result(property = "snsimageNo", column = "SNSIMAGE_NO"),
                        @Result(property = "snsNo", column = "SNS_NO") })
        public SnsImageDTO queryfindSnsImage(Long snsNo, int priority);

        // 이미지 정보 받아오기
        @Select({ "SELECT SNS_NO, SNSIMAGE_NO, IMAGENAME, PRIORITY FROM SNSIMAGE WHERE SNS_NO = #{snsNo}" })
        @Results({ @Result(property = "snsimageNo", column = "SNSIMAGE_NO"),
                        @Result(property = "snsNo", column = "SNS_NO") })
        public List<SnsImageDTO> queryfindSnsImagePriority(Long snsNo);
}
