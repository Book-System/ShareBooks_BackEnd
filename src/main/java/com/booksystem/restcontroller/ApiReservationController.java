package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booksystem.entity.Book;
import com.booksystem.entity.Member;
import com.booksystem.entity.Reservation;
import com.booksystem.entity.ReservationProjection;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.repository.ReservationRepository;
import com.booksystem.service.BookService;
import com.booksystem.service.MemberService;
import com.booksystem.service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/reservation")
public class ApiReservationController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationService reservationService;

    @Autowired
    MemberService memberService;

    @Autowired
    BookService bookService;

    // 책 예약
    // POST > http://localhost:9090/REST/api/reservation/register
    @RequestMapping(value = "/register", method = {
            RequestMethod.POST }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reservationRegisterPost(@ModelAttribute Reservation reservation,
            @RequestParam("bookNo") Long bookNo, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 로그인된 사용자를 검증
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // 토큰 값을 이용하여, 아이디를 추출
                String memberId = jwtUtil.extractUsername(token);

                // member객체와 book객체를 검색하여 저장
                Member member = memberService.getMember(memberId);
                Book book = bookService.detailBook(bookNo);

                // 예약 테이블에 Set메소드를 통해 member객체와 book객체를 저장
                reservation.setMember(member);
                reservation.setBook(book);

                // DB에 저장
                Reservation result = reservationService.registerReservation(reservation);
                if (result != null) {
                    map.put("result", 1L);
                    map.put("data", "책 예약을 성공했습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "책 예약을 실패했습니다.");
                }
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("result", "책 예약을 실패했습니다.");
        }
        return map;
    }

    // 빌린 책 목록 조회
    // GET > http://localhost:9090/REST/api/reservation/rent/list
    @RequestMapping(value = "/rent/list", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reservationListGet(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 로그인된 사용자를 검증
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // 토큰 값을 사용하여 아이디 추출
                String memberId = jwtUtil.extractUsername(token);

                // 자신이 빌린 책들의 정보를 조회
                List<ReservationProjection> list = reservationService.listReservation(memberId);
                map.put("result", 1L);
                map.put("data", list);
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "빌린 책 조회를 실패했습니다.");
        }
        return map;
    }

    // 빌린 책 상세 조회
    // GET > http://localhost:9090/REST/api/reservation/rent/detail?reservationno=5
    @RequestMapping(value = "/rent/detail", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reservationDetailGet(@RequestParam("reservationno") Long reservationno,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 로그인된 사용자를 검증
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // 자신이 빌린 책 중 한권 상세 조회
                ReservationProjection reservation = reservationService.detailReservation(reservationno);

                if (reservation != null) {
                    map.put("result", 1L);
                    map.put("data", reservation);
                } else {
                    map.put("result", 1L);
                    map.put("data", "예약정보가 존재하지 않습니다.");
                }
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "빌린 책 조회를 실패했습니다.");
        }
        return map;
    }

    // 책 예약 취소
    // DELETE > http://localhost:9090/REST/api/reservation/rent/delete
    @RequestMapping(value = "/rent/delete", method = {
            RequestMethod.DELETE }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reservationRentDelete(@RequestParam("reservationno") Long reservationno,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // deleteRent메소드 호출
                int result = reservationService.deleteReservation(reservationno);
                if (result == 1) {
                    map.put("result", 1L);
                    map.put("data", "책 예약 취소를 성공했습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "책 예약 취소를 실패했습니다.");
                }
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 예약 취소를 실패했습니다.");
        }
        return map;
    }

    // 판매자 => 요청 수락
    // PUT > http://localhost:9090/REST/api/reservation/request/accept
    @RequestMapping(value = "/request/accept", method = {
            RequestMethod.PUT }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reservationRequestAcceptPut(@RequestParam(name = "reservationno") Long reservationno,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                int result = reservationService.requestAcceptReservation(reservationno);
                if (result == 1) {
                    map.put("result", 1L);
                    map.put("data", "책 예약 요청 수락을 성공하였습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "책 예약 요청 수락을 실패하였습니다.");
                }
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 예약 요청 수락을 실패하였습니다.");
        }
        return map;
    }

    // 판매자 => 요청 거절, 거절 메세지
    // PUT > http://localhost:9090/REST/api/reservation/request/refuse
    @RequestMapping(value = "/request/refuse", method = {
            RequestMethod.PUT }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reservationRequestRefusePut(@RequestParam(name = "reservationno") Long reservationno,
            @RequestParam(name = "rejectmessage") String rejectmessage, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                int result = reservationService.requestRefuseReservation(reservationno, rejectmessage);
                if (result == 1) {
                    map.put("result", 1L);
                    map.put("data", "책 예약 요청 거절을 성공했습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "책 예약 요청 거절을 실패했습니다.");
                }
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 예약 요청 거절을 실패했습니다.");
        }
        return map;
    }
}