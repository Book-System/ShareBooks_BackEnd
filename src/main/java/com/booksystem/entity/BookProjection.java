package com.booksystem.entity;

// 이미지를 제외한 책의 정보를 가져오는 엔티티
public interface BookProjection {
    Long getBook_No();

    String getAddress();

    String getBook_Content();

    String getTitle();

    String getContent();

    String getHit();

    Long getPrice();

    String getRegdate();

    String getTag();

    String getBook_Title();

    String getMember_Id();

    String getMember_Name();

    String getMember_Nickname();

    String getCategory_Name();

    String getRating();

    Long getCount();
}