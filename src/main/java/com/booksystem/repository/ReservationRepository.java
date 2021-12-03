package com.booksystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.booksystem.entity.Reservation;
import com.booksystem.entity.ReservationBoardProjection;
import com.booksystem.entity.ReservationProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 예약 목록 조회
    @Query(value = "SELECT * FROM RESERVATION WHERE MEMBER_ID=:memberId", nativeQuery = true)
    public List<ReservationProjection> queryListReservation(@Param("memberId") String memberId);

    // 예약 개수 조회
    @Query(value = "SELECT COUNT(*) FROM RESERVATION WHERE MEMBER_ID=:memberId", nativeQuery = true)
    public int queryCountReservation(@Param("memberId") String memberId);

    // 예약 상세 조회
    @Query(value = "SELECT * FROM RESERVATION WHERE RESERVATION_NO=:reservationNo", nativeQuery = true)
    public ReservationProjection queryDetailReservation(@Param("reservationNo") Long reservationNo);

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

    // 빌려준 책 목록 조회
    @Query(value = "SELECT R.RESERVATION_NO, R.PHONE, R.REJECT_MESSAGE, R.REQUEST, R.REQUEST_MESSAGE, R.RESERVATION_END_DATE, R.RESERVATION_END_TIME, R.RESERVATION_START_DATE, R.RESERVATION_START_TIME, R.MEMBER_ID AS RMEMBER_ID,  B.BOOK_NO , B.MEMBER_ID AS BMEMBER_ID, B.ADDRESS, B.TITLE,  B.PRICE FROM RESERVATION R INNER JOIN BOOK B ON R.BOOK_NO=B.BOOK_NO WHERE B.MEMBER_ID=:memberId", nativeQuery = true)
    public List<ReservationProjection> queryRendBookList(@Param("memberId") String memberId);

    // 빌린 책 목록 조회
    @Query(value = "SELECT R.RESERVATION_NO, R.PHONE, R.REJECT_MESSAGE, R.REQUEST, R.REQUEST_MESSAGE, R.RESERVATION_END_DATE, R.RESERVATION_END_TIME, R.RESERVATION_START_DATE, R.RESERVATION_START_TIME, R.MEMBER_ID AS RMEMBER_ID,  B.BOOK_NO , B.MEMBER_ID AS BMEMBER_ID, B.ADDRESS, B.TITLE,  B.PRICE, R.PAY_SUCCESS FROM RESERVATION R INNER JOIN BOOK B ON R.BOOK_NO=B.BOOK_NO WHERE R.MEMBER_ID=:memberId", nativeQuery = true)
    public List<ReservationProjection> queryRentBookList(@Param("memberId") String memberId);

    // 대여자 => 결제완료
    @Transactional
    @Modifying
    @Query(value = "UPDATE RESERVATION SET PAY_SUCCESS=TRUE WHERE RESERVATION_NO=:reservationNo", nativeQuery = true)
    public int queryPaySuccess(@Param("reservationNo") Long reservationNo);

    // 예약 완료 조회
    @Query(value = "SELECT COUNT(PAY_SUCCESS) FROM RESERVATION WHERE BOOK_NO=:bookNo AND PAY_SUCCESS=TRUE", nativeQuery = true)
    public int queryReservationCheck(@Param("bookNo") Long bookNo);
<<<<<<< Updated upstream
=======

    // 추가----------------------------------------------------------------------------------------------------------------------------------------
    // 고객센터 글쓰기 시 예약번호 불러오기
    public Reservation findByReservationNo(Long reservationNo);

    // 예약 목록 조회(BOARD)
    @Query(value = "SELECT B.BOOK_NO, B.BOOK_TITLE, B.MEMBER_ID, R.RESERVATION_NO, R.RESERVATION_START_DATE FROM BOOK B INNER JOIN RESERVATION R ON B.BOOK_NO=R.BOOK_NO WHERE R.MEMBER_ID=:memberId GROUP BY(R.RESERVATION_NO)", nativeQuery = true)
    public List<ReservationBoardProjection> queryListReservationProjection(@Param("memberId") String memberId);
>>>>>>> Stashed changes
}