package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booksystem.entity.Book;
import com.booksystem.entity.Member;
import com.booksystem.entity.Review;
import com.booksystem.entity.ReviewProjection;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.service.BookService;
import com.booksystem.service.MemberService;
import com.booksystem.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/review")
public class ApiReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    MemberService memberService;

    @Autowired
    BookService bookService;

    @Autowired
    JwtUtil jwtUtil;

    // 리뷰 등록
    // POST > http://localhost:9090/REST/api/review/register
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reviewRegisterPost(@RequestBody Map<String, Object> obj,
            @RequestHeader("token") String token) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            // Member객체, Book객체
            String memberId = jwtUtil.extractUsername(token);
            Member member = memberService.getMember(memberId);
            Book book = bookService.detailBook(Long.parseLong(String.valueOf(obj.get("book"))));

            // Review객체
            Review review = new Review();
            review.setContent(String.valueOf(obj.get("content")));
            review.setRating(Float.parseFloat(String.valueOf(obj.get("rating"))));
            review.setMember(member);
            review.setBook(book);

            // registerReview메소드 호출
            int result = reviewService.registerReview(review);

            if (result == 1) {
                map.put("result", 1L);
                map.put("data", "리뷰 등록을 성공했습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "리뷰 등록을 실패했습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "리뷰 등록을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 리뷰 삭제
    // DELETE > http://localhost:9090/REST/api/review/remove
    @RequestMapping(value = "/remove", method = RequestMethod.DELETE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reviewRemoveDelete(@RequestBody Map<String, Object> obj,
            @RequestHeader("token") String token) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            Long reviewNo = Long.parseLong(String.valueOf(obj.get("reviewNo")));

            // removeReview메소드 호출
            int result = reviewService.removeReview(reviewNo);

            if (result == 1) {
                map.put("result", 1L);
                map.put("data", "리뷰 삭제를 성공했습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "리뷰 삭제를 실패했습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "리뷰 삭제를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 리뷰 수정(reviewNo, rating, content)
    // PUT > http://localhost:9090/REST/api/review/update
    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reviewUpdatePut(@RequestBody Map<String, Object> obj,
            @RequestHeader("token") String token) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            Review review = reviewService.reviewGet(Long.parseLong(String.valueOf(obj.get("reviewNo"))));
            review.setContent(String.valueOf(obj.get("content")));
            review.setRating(Float.parseFloat(String.valueOf(obj.get("rating"))));

            // updateReview메소드 호출
            int result = reviewService.updateReview(review);

            if (result == 1) {
                map.put("result", 1L);
                map.put("data", "리뷰 수정을 성공했습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "리뷰 수정을 실패했습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "리뷰 수정 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 리뷰 상세 조회
    // GET > http://localhost:9090/REST/api/review/detail
    @RequestMapping(value = "/detail", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reviewDetailGet(@RequestParam Long reviewno, @RequestHeader("token") String token)
            throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            // detailReview메소드 호출
            ReviewProjection review = reviewService.detailReview(reviewno);

            map.put("result", 1L);
            map.put("review", review);
            map.put("data", "리뷰 조회를 성공했습니다.");
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "리뷰 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 리뷰 목록 조회
    // GET > http://localhost:9090/REST/api/review/list
    @RequestMapping(value = "/list", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reviewListGet(@RequestHeader("token") String token) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                String memberId = jwtUtil.extractUsername(token);

                // listReview메소드 호출
                List<ReviewProjection> list = reviewService.listReview(memberId);

                map.put("result", 1L);
                map.put("list", list);
                map.put("data", "리뷰 목록 조회를 성공했습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "리뷰 목록 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 리뷰 개수(1개 이상은 등록이 안되도록..)
    // GET > http://localhost:9090/REST/api/review/count
    @RequestMapping(value = "/count", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reviewCountGet(@RequestParam("bookno") Long bookNo, @RequestHeader("token") String token)
            throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            String memberId = jwtUtil.extractUsername(token);
            // countReview메소드 호출
            int result = reviewService.countReview(memberId, bookNo);

            map.put("result", 1L);
            map.put("data", result);
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "리뷰 목록 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 리뷰 평균 점수, 개수 조회
    // GET > http://localhost:9090/REST/api/review/avg?bookno=책번호
    @RequestMapping(value = "/avg", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reviewAvgGet(@RequestParam("bookno") Long bookNo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            // avgReview메소드 호출
            ReviewProjection result = reviewService.avgReview(bookNo);
            map.put("result", 1L);
            map.put("data", result);
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "리뷰 평균 점수, 개수 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 책 번호에 맞는 리뷰 목록 조회
    // GET > http://localhost:9090/REST/api/review/match/list?bookno=책번호
    @RequestMapping(value = "/match/list", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reviewBookNoGet(@RequestParam("bookno") Long bookNo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            // listReview메소드 호출
            List<Review> list = reviewService.bookNoReview(bookNo);
            map.put("result", 1L);
            map.put("list", list);
            map.put("data", "책 번호에 해당하는 리뷰 목록 조회를 성공했습니다.");
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 번호에 해당하는 리뷰 목록 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 자신이 작성한 리뷰 개수 조회
    // GET > http://localhost:9090/REST/api/review/mycount
    @RequestMapping(value = "/mycount", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> reviewMyCountGet(@RequestHeader("token") String token) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            String memberId = jwtUtil.extractUsername(token);
            // countMyReview메소드 호출
            int count = reviewService.countMyReview(memberId);
            map.put("result", 1L);
            map.put("data", count);
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "리뷰 개수 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }
}