package com.booksystem.entity;

public interface ReviewProjection {
    Long getReview_No();

    float getRating();

    Long getCount();

    String getTitle();

    String getContent();

    String getRegdate();

    String getMember_Id();

    Long getBook_No();
}