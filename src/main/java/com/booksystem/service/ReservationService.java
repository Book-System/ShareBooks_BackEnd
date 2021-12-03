package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Reservation;
import com.booksystem.entity.ReservationBoardProjection;
import com.booksystem.entity.ReservationProjection;

import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

    // 예약 목록 조회
    public List<ReservationProjection> listReservation(String memberId);

    // 예약 개수 조회
    public int CountReservation(String memberId);

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

    // 빌려준 책 목록 조회
    public List<ReservationProjection> rendBookList(String memberId);

    // 빌린 책 목록 조회
    public List<ReservationProjection> rentBookList(String memberId);

    // 대여자 => 결제완료
    public int paySuccess(Long reservationNo);

    // 예약완료 체크
    public int reservationCheck(Long bookNo);

    // 추가----------------------------------------------------------------------------------------------------------------------------------------
    // 고객센터 글쓰기 시 예약번호 불러오기
    public Reservation findByReservationNo(Long reservationNo);

    // 예약 목록 조회(BOARD)
    public List<ReservationBoardProjection> listReservationProjection(String memberId);
}