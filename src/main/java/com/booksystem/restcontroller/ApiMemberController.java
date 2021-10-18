package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.booksystem.entity.Member;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.repository.MemberRepository;
import com.booksystem.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/member")
public class ApiMemberController {

    @Autowired
    MemberService mService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    // 회원가입 (아이디, 비밀번호, 이름, 닉네임, 휴대폰번호, 우편번호, 우편주소, 회원가입날짜, 프로필사진(기본이미지로 처리))
    // POST > http://localhost:9090/REST/api/member/join
    @RequestMapping(value = "/join", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> memberJoinPost(@RequestBody Member member) {
        // 결과를 리턴하기 위해 map변수 선언
        Map<String, Long> map = new HashMap<>();
        try {
            // 비밀번호 암호화
            BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
            member.setPassword(bcpe.encode(member.getPassword()));

            // DB에 member객체의 데이터를 저장
            mService.joinMember(member);

            // 결과 값으로 1을 저장한다.
            map.put("result", 1L);
        } catch (Exception e) {
            // 에러를 출력
            e.printStackTrace();

            // 결과 값으로 0을 저장한다.
            map.put("result", 0L);
        }
        // 결과 값 리턴
        return map;
    }

    // 로그인 (아이디, 비밀번호)
    // POST > http://localhost:9090/REST/api/member/login
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberLoginPost(@RequestBody Member member) {
        System.out.println(member.toString());
        // 결과를 리턴하기 위해 map변수 선언
        Map<String, Object> map = new HashMap<>();
        try {
            // 비밀번호 암호화
            // BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
            // member.setPassword(bcpe.encode(member.getPassword()));
            System.out.println(member.getPassword());
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(member.getId(), member.getPassword()));
            // 결과 값으로 1을 저장한다.
            map.put("result", 1L);
            map.put("token", jwtUtil.generateToken(member.getId()));

        } catch (Exception e) {
            // 에러를 출력
            e.printStackTrace();
            // 결과 값으로 0을 저장한다.
            map.put("result", 0L);
        }
        // 결과 값 리턴
        return map;
    }
}
