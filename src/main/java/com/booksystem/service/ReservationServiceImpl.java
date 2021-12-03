package com.booksystem.service;

import java.util.List;

import com.booksystem.entity.Reservation;
import com.booksystem.entity.ReservationBoardProjection;
import com.booksystem.entity.ReservationProjection;
import com.booksystem.repository.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Override
    public List<ReservationProjection> listReservation(String memberId) {
        return reservationRepository.queryListReservation(memberId);
    }

    @Override
    public Reservation registerReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public int deleteReservation(Long reservationNo) {
        return reservationRepository.queryDeleteReservation(reservationNo);
    }

    @Override
    public int requestAcceptReservation(Long reservationNo) {
        return reservationRepository.queryRequestAcceptReservation(reservationNo);
    }

    @Override
    public int requestRefuseReservation(Long reservationNo, String rejectMessage) {
        return reservationRepository.queryRequestRefuseReservation(reservationNo, rejectMessage);
    }

    @Override
    public ReservationProjection detailReservation(Long reservationNo) {
        return reservationRepository.queryDetailReservation(reservationNo);
    }

    @Override
    public int CountReservation(String memberId) {
        return reservationRepository.queryCountReservation(memberId);
    }

    @Override
    public List<ReservationProjection> rendBookList(String memberId) {
        return reservationRepository.queryRendBookList(memberId);
    }

    @Override
    public List<ReservationProjection> rentBookList(String memberId) {
        return reservationRepository.queryRentBookList(memberId);
    }

    @Override
    public int paySuccess(Long reservationNo) {
        return reservationRepository.queryPaySuccess(reservationNo);
    }

    @Override
    public int reservationCheck(Long bookNo) {
        return reservationRepository.queryReservationCheck(bookNo);
    }

    // 추가----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Reservation findByReservationNo(Long reservationNo) {
        return reservationRepository.findByReservationNo(reservationNo);
    }

    @Override
    public List<ReservationBoardProjection> listReservationProjection(String memberId) {
        return reservationRepository.queryListReservationProjection(memberId);
    }
}