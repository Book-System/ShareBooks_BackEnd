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
import org.springframework.web.bind.annotation.RequestBody;
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
    public Map<String, Object> reservationRegisterPost(@RequestBody Map<String, Object> obj,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            System.out.println(obj.toString());
            // 로그인된 사용자를 검증
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // 입력받은 값 추출
                Long bookNo = Long.parseLong(String.valueOf(obj.get("bookNo")));
                String phone = String.valueOf(obj.get("phone"));
                String startDate = String.valueOf(obj.get("reservationStartDate"));
                String endDate = String.valueOf(obj.get("reservationEndDate"));
                String startTime = String.valueOf(obj.get("reservationStartTime"));
                String endTime = String.valueOf(obj.get("reservationEndTime"));
                String request = String.valueOf(obj.get("requestMessage"));

                // 토큰 값을 이용하여, 아이디를 추출
                String memberId = jwtUtil.extractUsername(token);

                // member객체와 book객체를 검색하여 저장
                Member member = memberService.getMember(memberId);
                Book book = bookService.detailBook(bookNo);

                // Reservation 객체 생성, 저장
                Reservation reservation = new Reservation();
                reservation.setMember(member);
                reservation.setBook(book);
                reservation.setPhone(phone);
                reservation.setReservationStartDate(startDate);
                reservation.setReservationEndDate(endDate);
                reservation.setReservationStartTime(startTime);
                reservation.setReservationEndTime(endTime);
                reservation.setRequestMessage(request);

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

    // 빌린 책 개수 조회
    // GET > http://localhost:9090/REST/api/reservation/rent/count
    @RequestMapping(value = "/rent/count", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reservationCountGet(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 로그인된 사용자를 검증
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // 토큰 값을 사용하여 아이디 추출
                String memberId = jwtUtil.extractUsername(token);

                // 자신이 빌린 책들의 정보를 조회
                int rentBookCount = reservationService.CountReservation(memberId);
                map.put("result", 1L);
                map.put("data", rentBookCount);
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 개수 조회를 실패했습니다.");
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
    public Map<String, Object> reservationRequestAcceptPut(@RequestBody Map<String, Object> obj,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                Long reservationno = Long.parseLong(String.valueOf(obj.get("reservationno")));
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
    public Map<String, Object> reservationRequestRefusePut(@RequestBody Map<String, Object> obj,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // 예약번호, 거절메세지 받아오기
                Long reservationno = Long.parseLong(String.valueOf(obj.get("reservationno")));
                String rejectmessage = String.valueOf(obj.get("rejectmessage"));

                // requestRefuseReservation메서드 호출
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

    // 빌려준 책 목록 조회
    // GET > http://localhost:9090/REST/api/reservation/list/rend
    @RequestMapping(value = "/list/rend", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> listRendBookGet(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                String memberId = jwtUtil.extractUsername(token);
                List<ReservationProjection> rendBookList = reservationService.rendBookList(memberId);
                map.put("result", 1L);
                map.put("data", rendBookList);
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "빌려준 책 목록 조회를 실패했습니다.");
        }
        return map;
    }

    // 빌린 책 목록 조회
    // GET > http://localhost:9090/REST/api/reservation/list/rent
    @RequestMapping(value = "/list/rent", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> listRentBookGet(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                String memberId = jwtUtil.extractUsername(token);
                List<ReservationProjection> rentBookList = reservationService.rentBookList(memberId);
                map.put("result", 1L);
                map.put("data", rentBookList);
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "빌린 책 목록 조회를 실패했습니다.");
        }
        return map;
    }

    // 대여자 => 결제완료
    // PUT > http://localhost:9090/REST/api/reservation/pay/success
    @RequestMapping(value = "/pay/success", method = {
            RequestMethod.PUT }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reservationPaySuccessPut(@RequestBody Map<String, Object> obj,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                Long reservationno = Long.parseLong(String.valueOf(obj.get("reservationno")));

                int result = reservationService.paySuccess(reservationno);
                if (result == 1) {
                    map.put("result", 1L);
                    map.put("data", "책 결제를 성공하였습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "책 결제를 실패하였습니다.");
                }
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 결제를 실패하였습니다.");
        }
        return map;
    }
}