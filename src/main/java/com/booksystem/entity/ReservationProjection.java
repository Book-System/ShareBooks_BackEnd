package com.booksystem.entity;

public interface ReservationProjection {
    Long getReservation_No();

    String getPhone();

    String getReservation_Start_Date();

    String getReservation_End_Date();

    String getReservation_Start_Time();

    String getReservation_End_Time();

    String getRequest_Message();

    String getMember_Id();

    Long getBook_No();
}