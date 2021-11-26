package com.booksystem.entity;

public interface ReservationProjection {
    Long getReservation_No();

    String getPhone();

    String getReservation_Start_Date();

    String getReservation_End_Date();

    String getReservation_Start_Time();

    String getReservation_End_Time();

    String getRequest_Message();

    String getRmember_Id();

    String getBmember_Id();

    String getReject_Message();

    Boolean getRequest();

    String getAddress();

    String getTitle();

    Boolean getPay_Success();

    Long getPrice();

    Long getBook_No();
}