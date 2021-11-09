package com.booksystem.entity;

import java.util.Date;

// 이미지를 제외한 책의 정보를 가져오는 엔티티
public interface BookProjection {
    Long getBook_No();

    String getTitle();

    String getContent();

    Long getPrice();

    String getAddress();

    String getTag();

    String getBook_Title();

    String getBook_Content();

    Date getRegdate();
}