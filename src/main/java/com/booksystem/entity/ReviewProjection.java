package com.booksystem.entity;

public interface ReviewProjection {
    Long getReview_No();

    int getRating();

    String getContent();

    String getRegdate();

    String getMember_Id();

    Long getBook_No();
}