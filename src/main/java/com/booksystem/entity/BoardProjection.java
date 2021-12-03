package com.booksystem.entity;

import java.util.Date;

public interface BoardProjection {
    Long getBoard_No();

    String getContent();

    String getInquiry();

    String getTitle();

    String getMember_ID();

    Date getRegdate();

    Long getReservation_No();

}
