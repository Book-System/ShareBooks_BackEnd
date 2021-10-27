package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.booksystem.entity.Book;
import com.booksystem.entity.Category;
import com.booksystem.entity.Member;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.service.BookService;
import com.booksystem.service.CategoryService;
import com.booksystem.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/book")
public class ApiBookController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    BookService bService;

    @Autowired
    MemberService mService;

    @Autowired
    CategoryService cService;

    // 책 등록 => 문제가 생기면 이부분 삭제
    // POST > http://localhost:9090/REST/api/book/register
    @RequestMapping(value = "/register", method = {
            RequestMethod.POST }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> bookRegisterPost(@ModelAttribute Book book,
            @RequestParam(name = "categoryCode") Long categoryCode, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 로그인된 사용자를 검증
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // 토큰 값을 이용하여, 아이디를 추출
                String memberId = jwtUtil.extractUsername(token);
                // memberId를 조건으로 member객체 추출
                Member member = mService.getMember(memberId);
                // categoryCode를 조건으로 category객체 추출
                Category category = cService.detailCategory(categoryCode);

                // Member객체와 Category객체 설정
                book.setMember(member);
                book.setCategory(category);

                Book result = bService.registerBook(book);

                //
                System.out.println(result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("result", "도서 등록을 실패했습니다.");
        }
        return map;
    }
}
