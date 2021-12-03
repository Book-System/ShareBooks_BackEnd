package com.booksystem.dto;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Getter
@NoArgsConstructor
@ToString
public class SnsReplyDTO {
    private Long snsreplyNo;
    private String content;
    private Date regdate;
    private Long snsNo;
    private String member;
    private String memberName; // nickname
}

// CREATE TABLE SNSREPLY( SNSREPLY_NO NUMBER PRIMARY KEY, CONTENT VARCHAR2(100),
// REGDATE
// TIMESTAMP, SNS_NO NUMBER, MEMBER_ID VARCHAR2(50), MEMBER_NAME VARCHAR2(50)
// NOT NULL );

// CREATE
// SEQUENCE SEQ_SNSREPLY_NO
// START WITH 1
// INCREMENT BY 1
// NOMAXVALUE NOCACHE;

// ALTER TABLE SNSREPLY ADD FOREIGN KEY(SNS_NO) REFERENCES SNS(SNS_NO);
// ALTER TABLE SNSREPLY ADD FOREIGN KEY(MEMBER_ID) REFERENCES MEMBER(ID);
