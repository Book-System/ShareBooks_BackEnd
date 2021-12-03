package com.booksystem.mappers;

import com.booksystem.dto.SnsGoodDTO;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SnsGoodMapper {
        // 우리동네 좋아요(따봉)
        @Insert({ "INSERT INTO SNSGOOD(SNSGOOD_NO, SNS_NO, MEMBER_ID)",
                        "VALUES (SEQ_SNSGOOD_NO.NEXTVAL, #{snsNo}, #{member})" })
        public int queryInsertGoodSns(SnsGoodDTO snsgood);

        // 우리동네 좋아요취소(따봉삭제)
        @Delete({ "DELETE FROM SNSGOOD WHERE SNS_NO=#{snsNo} AND MEMBER_ID = #{memberId}" })
        public int queryDeleteGoodSns(Long snsNo, String memberId);

        // 우리동네 좋아요 개수 체크(내가 했는지 안했는지)
        @Select({ "SELECT COUNT(*) FROM SNSGOOD WHERE SNS_NO=#{snsNo} AND MEMBER_ID=#{memberId}" })
        public int queryCheckGoodSns(Long snsNo, String memberId);

        // 우리동네 글삭제 시 좋아요 삭제
        @Delete({ "DELETE FROM SNSGOOD WHERE SNS_NO=#{snsNo}" })
        public int queryDeleteGoodsSns(Long snsNo);

}
