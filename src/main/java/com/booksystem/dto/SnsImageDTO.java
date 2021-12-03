package com.booksystem.dto;

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
public class SnsImageDTO {
    private Long snsimageNo;
    private byte[] image;
    private String imagename;
    private Long imagesize;
    private String imagetype;
    private int priority;
    private Long snsNo;
}

// CREATE TABLE SNSIMAGE( SNSIMAGE_NO NUMBER PRIMARY KEY, IMAGE BLOB, IMAGENAME
// VARCHAR2(100), IMAGESIZE NUMBER , IMAGETYPE VARCHAR2(100), PRIORITY NUMBER,
// SNS_NO NUMBER);

// CREATE
// SEQUENCE SEQ_SNSIMAGE_NO
// START WITH 1
// INCREMENT BY 1
// NOMAXVALUE NOCACHE;

// ALTER TABLE SNSIMAGE ADD FOREIGN KEY(SNS_NO) REFERENCES SNS(SNS_NO);