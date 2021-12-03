package com.booksystem.dto;

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
public class SnsGoodDTO {
    private Long snsgoodNo;
    private Long snsNo;
    private String member;
}

// CREATE TABLE SNSGOOD( SNSGOOD_NO NUMBER PRIMARY KEY,
// SNS_NO NUMBER, MEMBER_ID VARCHAR2(100) NOT NULL );

// CREATE
// SEQUENCE SEQ_SNSGOOD_NO
// START WITH 1
// INCREMENT BY 1
// NOMAXVALUE NOCACHE;

// ALTER TABLE SNSGOOD ADD FOREIGN KEY(SNS_NO) REFERENCES SNS(SNS_NO);
// ALTER TABLE SNSGOOD ADD FOREIGN KEY(MEMBER_ID) REFERENCES MEMBER(id);
