package com.booksystem.entity;

public interface ReservationBoardProjection {
    Long getBook_No();

    String getBook_Title();

    String getMember_Id();

    String getReservation_Start_Date();

    Long getReservation_No();
}
