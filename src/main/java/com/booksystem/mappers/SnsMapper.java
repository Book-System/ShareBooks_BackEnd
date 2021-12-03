package com.booksystem.mappers;

import java.util.List;
import java.util.Optional;

import com.booksystem.dto.SnsDTO;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SnsMapper {

        // 우리동네 글쓰기
        @Options(useGeneratedKeys = true, keyProperty = "snsNo", keyColumn = "SNS_NO")
        @Insert({ "INSERT INTO SNS(SNS_NO, TITLE, CONTENT, ADDRESS, REGDATE, MEMBER_ID, MEMBER_NAME)",
                        "VALUES (SEQ_SNS_NO.NEXTVAL, #{title}, #{content}, #{address}, CURRENT_TIMESTAMP, #{member}, #{memberName})" })
        public int queryInsertSns(SnsDTO sns);

        // 우리동네 글목록(전체조회)
        @Select({ "SELECT S.SNS_NO, S.TITLE, S.CONTENT, S.ADDRESS, S.REGDATE, S.MEMBER_ID, S.MEMBER_NAME, I.COUNT, G.GOOD, F.PLEASE, Y.REPLY FROM SNS S LEFT OUTER JOIN (SELECT SNS_NO, COUNT(*) AS COUNT FROM SNSIMAGE GROUP BY(SNS_NO)) I ON S.SNS_NO=I.SNS_NO LEFT OUTER JOIN (SELECT SNS_NO, COUNT(*) AS GOOD FROM SNSGOOD GROUP BY(SNS_NO)) G ON I.SNS_NO=G.SNS_NO LEFT OUTER JOIN (SELECT SNS_NO, COUNT(*) AS PLEASE FROM SNSGOOD WHERE MEMBER_ID = #{memberId} GROUP BY (SNS_NO)) F ON G.SNS_NO=F.SNS_NO LEFT OUTER JOIN (SELECT SNS_NO, COUNT(*) AS REPLY FROM SNSREPLY GROUP BY(SNS_NO)) Y ON S.SNS_NO=Y.SNS_NO ORDER BY S.SNS_NO DESC" })
        @Results({ @Result(property = "snsNo", column = "SNS_NO"), @Result(property = "member", column = "MEMBER_ID"),
                        @Result(property = "memberName", column = "MEMBER_NAME") })
        public List<SnsDTO> querySelectSnsAll(String memberId);

        // 우리동네 글수정 시 글목록
        @Select({ "SELECT SNS_NO, TITLE, CONTENT FROM SNS WHERE SNS_NO = ${snsNo} ORDER BY SNS_NO DESC" })
        @Results({ @Result(property = "snsNo", column = "SNS_NO") })
        public Optional<SnsDTO> querySelectSns(Long snsNo);

        // 우리동네 글삭제
        @Delete({ "DELETE FROM SNS WHERE SNS_NO=#{snsNo}" })
        public int queryDeleteSns(Long snsNo);

        // 우리동네 글수정
        @Update({ "UPDATE SNS SET TITLE=#{title}, CONTENT=#{content} WHERE SNS_NO=#{snsNo}" })
        public int queryUpdateSns(SnsDTO sns);

        // 우리동네 좋아요 목록
        @Select({ "SELECT S.SNS_NO, G.GOOD, F.PLEASE, Y.REPLY FROM SNS S LEFT OUTER JOIN (SELECT SNS_NO, COUNT(*) AS GOOD FROM SNSGOOD GROUP BY(SNS_NO)) G ON S.SNS_NO=G.SNS_NO LEFT OUTER JOIN (SELECT SNS_NO, COUNT(*) AS PLEASE FROM SNSGOOD WHERE MEMBER_ID =#{memberId} GROUP BY (SNS_NO)) F ON G.SNS_NO=F.SNS_NO LEFT OUTER JOIN (SELECT SNS_NO, COUNT(*) AS REPLY FROM SNSREPLY GROUP BY(SNS_NO)) Y ON S.SNS_NO=Y.SNS_NO ORDER BY S.SNS_NO DESC" })
        @Results({ @Result(property = "snsNo", column = "SNS_NO") })
        public List<SnsDTO> querySelectSnsGood(String memberId);

}
