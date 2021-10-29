package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Reservation;

import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

    // 예약 목록 조회
    public List<Reservation> listReservation(String memberId);

    // 예약 정보 등록
    public Reservation registerReservation(Reservation reservation);

    // 예약 정보 삭제
    public int deleteReservation(Long reservationNo);
}
