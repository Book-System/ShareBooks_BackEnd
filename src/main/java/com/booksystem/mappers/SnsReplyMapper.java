package com.booksystem.mappers;

import java.util.List;

import com.booksystem.dto.SnsReplyDTO;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SnsReplyMapper {
        // 우리동네 댓글 쓰기
        @Insert({ "INSERT INTO SNSREPLY(SNSREPLY_NO, CONTENT, REGDATE, SNS_NO, MEMBER_ID, MEMBER_NAME) ",
                        "VALUES (SEQ_SNSREPLY_NO.NEXTVAL, #{content}, CURRENT_TIMESTAMP, #{snsNo}, #{member}, #{memberName})" })
        public int queryInsertReplySns(SnsReplyDTO snsreply);

        // 우리동네 댓글 조회
        @Results({ @Result(property = "member", column = "MEMBER_ID"),
                        @Result(property = "memberName", column = "MEMBER_NAME"),
                        @Result(property = "snsNo", column = "SNS_NO"),
                        @Result(property = "snsreplyNo", column = "SNSREPLY_NO") })
        @Select({ "SELECT * FROM SNSREPLY WHERE SNS_NO = #{snsNo} ORDER BY SNSREPLY_NO DESC" })
        public List<SnsReplyDTO> querySelectReplySns(Long snsNo);

        // 우리동네 댓글 삭제
        @Delete({ "DELETE FROM SNSREPLY WHERE SNSREPLY_NO=#{snsreplyNo}" })
        public int queryDeleteReplySns(Long snsreplyNo);

        // 우리동네 댓글 수정
        @Update({ "UPDATE SNSREPLY SET CONTENT=#{content} WHERE SNSREPLY_NO=#{snsreplyNo}" })
        public int queryUpdateReplySns(SnsReplyDTO snsreply);

        // 우리동네 글삭제 시 댓글 삭제
        @Delete({ "DELETE FROM SNSREPLY WHERE SNS_NO=#{snsNo}" })
        public int queryDeleteRepliesSns(Long snsNo);
}
