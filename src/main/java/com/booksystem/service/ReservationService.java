package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Reservation;
import com.booksystem.entity.ReservationProjection;

import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

    // 예약 목록 조회
    public List<ReservationProjection> listReservation(String memberId);

    // 예약 상세 조회
    public ReservationProjection detailReservation(Long reservationNo);

    // 예약 정보 등록
    public Reservation registerReservation(Reservation reservation);

    // 예약 정보 삭제
    public int deleteReservation(Long reservationNo);

    // 판매자 => 요청 수락
    public int requestAcceptReservation(Long reservationNo);

    // 판매자 => 요청 거절, 거절 메세지
    public int requestRefuseReservation(Long reservationNo, String rejectMessage);
}