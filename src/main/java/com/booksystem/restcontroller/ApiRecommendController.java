package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booksystem.entity.RecommendBook;
import com.booksystem.entity.RecommendBookProjection;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.service.BookService;
import com.booksystem.service.CategoryService;
import com.booksystem.service.MemberService;
import com.booksystem.service.RecommendService;
import com.booksystem.service.ReservationService;
import com.booksystem.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/recommend")
public class ApiRecommendController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MemberService memberService;

    @Autowired
    BookService bookService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    RecommendService recommendService;

    // 베스트 셀러 추천 책 목록 조회(5권) => N번 이상 대여된 항목에 대해, 카테고리별 평점이 높은 항목 5권 추출
    // GET > http://localhost:9090/REST/api/recommend/category/book
    @RequestMapping(value = "/category/book", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> RecommendCategoryBookGet(@RequestParam("categorycode") Long categoryCode) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<RecommendBook> recommendBooks = recommendService.categoryRecommendBooks(categoryCode);
            if (recommendBooks.size() > 0) {
                map.put("result", 1L);
                map.put("data", recommendBooks);
            } else {
                map.put("result", 1L);
                map.put("data", "이달의 대여 목록 조회를 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "이달의 대여 목록 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 베스트 셀러 추천 책 목록 조회(6권) => N번 이상 대여된 항목에 대해, 카테고리별 평점이 높은 항목 6권 추출
    // GET > http://localhost:9090/REST/api/recommend/all/book
    @RequestMapping(value = "/all/book", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> RecommendAllBookGet() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<RecommendBook> recommendBooks = recommendService.allRecommendBooks();
            if (recommendBooks.size() > 0) {
                map.put("result", 1L);
                map.put("data", recommendBooks);
            } else {
                map.put("result", 1L);
                map.put("data", "이달의 대여 목록 조회를 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "이달의 대여 목록 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 평점이 높은 책 8권 가져오기
    // GET > http://localhost:9090/REST/api/recommend/rating/book
    @RequestMapping(value = "/rating/book", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> RecommendRatingBookGet() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<RecommendBookProjection> recommendBooks = recommendService.recommendRatingBooks();
            if (recommendBooks.size() > 0) {
                map.put("result", 1L);
                map.put("data", recommendBooks);
            } else {
                map.put("result", 1L);
                map.put("data", "평점이 높은 책 목록 조회를 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "평점이 높은 책 목록 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }
<<<<<<< Updated upstream
=======

    // 최신 후기 6개 가져오기
    // GET > http://localhost:9090/REST/api/recommend/comment/book
    @RequestMapping(value = "/comment/book", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> RecommendCommentBookGet() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<RecommendBookProjection> recommendBooks = recommendService.commentBooks();
            if (recommendBooks.size() > 0) {
                map.put("result", 1L);
                map.put("data", recommendBooks);
            } else {
                map.put("result", 1L);
                map.put("data", "최신 후기 조회를 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "최신 후기 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 이달의 책 6권 가져오기
    // GET > http://localhost:9090/REST/api/recommend/month/book
    @RequestMapping(value = "/month/book", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> RecommendMonthBookGet() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<RecommendBookProjection> recommendBooks = recommendService.monthBooks();
            if (recommendBooks.size() > 0) {
                map.put("result", 1L);
                map.put("data", recommendBooks);
            } else {
                map.put("result", 1L);
                map.put("data", "평점이 높은 책 목록 조회를 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "평점이 높은 책 목록 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }
>>>>>>> Stashed changes
}