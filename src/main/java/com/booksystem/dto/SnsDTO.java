package com.booksystem.dto;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SnsDTO {
    private Long snsNo;
    private String title;
    private String content;
    private String address;
    private Date regdate;
    private String member;
    // inner join
    private String memberName;
    private Long count = null;
    private Long good = null;
    private Long please = null;
    private Long reply = null;
}

// CREATE TABLE SNS( SNS_NO NUMBER PRIMARY KEY,TITLE VARCHAR2(50), CONTENT
// VARCHAR2(300), ADDRESS VARCHAR2(50), REGDATE TIMESTAMP, MEMBER_ID
// VARCHAR2(100) NOT NULL, MEMBER_NAME VARCHAR2(50), COUNT
// NUMBER, GOOD NUMBER, GOODCHECK NUMBER, REPLY NUMBER );

// CREATE
// SEQUENCE SEQ_SNS_NO
// START WITH 1
// INCREMENT BY 1
// NOMAXVALUE NOCACHE;

// ALTER TABLE SNS ADD FOREIGN KEY(MEMBER_ID) REFERENCES MEMBER(id);
