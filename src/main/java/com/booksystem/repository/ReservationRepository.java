package com.booksystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.booksystem.entity.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 예약 목록 조회
    @Query(value = "SELECT * FROM RESERVATION WHERE MEMBER_ID=:memberId", nativeQuery = true)
    public List<Reservation> queryListReservation(@Param("memberId") String memberId);

    // 예약 정보 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM RESERVATION WHERE RESERVATION_NO=:reservationNo", nativeQuery = true)
    public int queryDeleteReservation(@Param("reservationNo") Long reservationNo);

    // 판매자 => 요청 수락
    @Transactional
    @Modifying
    @Query(value = "UPDATE RESERVATION SET REQUEST=true WHERE RESERVATION_NO=:reservationNo", nativeQuery = true)
    public int queryRequestAcceptReservation(@Param("reservationNo") Long reservationNo);

    // 판매자 => 요청 거절, 거절 메세지
    @Transactional
    @Modifying
    @Query(value = "UPDATE RESERVATION SET REJECT_MESSAGE=:rejectMessage WHERE RESERVATION_NO=:reservationNo", nativeQuery = true)
    public int queryRequestRefuseReservation(@Param("reservationNo") Long reservationNo,
            @Param("rejectMessage") String rejectMessage);

}