package com.booksystem.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.booksystem.entity.Member;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/member")
public class ApiMemberController {

    @Autowired
    MemberService mService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    AuthenticationManager authenticationManager;

    // 회원가입 (아이디, 비밀번호, 이름, 닉네임, 휴대폰번호, 우편번호, 우편주소, 회원가입날짜, 프로필사진)
    // POST > http://localhost:9090/REST/api/member/join
    @RequestMapping(value = "/join", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberJoinPost(@ModelAttribute Member member,
            @RequestParam(name = "file") MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {
            // 비밀번호 암호화
            BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
            member.setPassword(bcpe.encode(member.getPassword()));

            // 이미지 설정
            member.setImage(file.getBytes());
            member.setImagesize(file.getSize());
            member.setImagetype(file.getContentType());

            // joinMember메소드 호출을 통해 회원가입 수행
            mService.joinMember(member);
            map.put("result", 1L);
            map.put("data", "회원가입을 성공했습니다.");
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "회원가입을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 회원탈퇴
    // DELETE > http://localhost:9090/REST/api/member/withdrawal
    @RequestMapping(value = "/withdrawal", method = RequestMethod.DELETE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberWithdrawalDelete(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰을 사용하여, 아이디를 추출한다.
            String memberId = jwtUtil.extractUsername(token);

            // deleteMember메서드를 호출한다.
            int result = mService.deleteMember(memberId);
            map.put("result", Long.valueOf(result));
            map.put("data", "회원탈퇴를 성공했습니다.");
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "회원탈퇴를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 회원정보수정 (닉네임, 휴대폰번호, 우편번호, 우편주소, 프로필사진)
    // PUT > http://localhost:9090/REST/api/member/update
    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberUpdatePut(@ModelAttribute Member member, @RequestHeader("token") String token,
            @RequestParam(name = "file") MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰이 생성되었고, 유효시간이 만료되지 않았다면 회원정보수정 실행
            if (token != "" && !jwtUtil.isTokenExpired(token)) {
                int result = 0;
                // 프로필 사진을 변경할 경우
                if (file.getSize() > 0) {
                    // 이미지 설정
                    member.setImage(file.getBytes());
                    member.setImagesize(file.getSize());
                    member.setImagetype(file.getContentType());
                    result = mService.updateMemberWithImage(member);
                }
                // 프로필 사진을 변경하지 않을 경우
                else {
                    result = mService.updateMember(member);
                }
                map.put("result", Long.valueOf(result));
                map.put("data", "회원정보수정을 성공했습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "회원정보수정을 실패했습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "회원정보수정을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 아이디 중복확인
    // GET > http://localhost:9090/REST/api/member/checkid?id=회원아이디
    @RequestMapping(value = "/checkid", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberCheckIdGet(@RequestParam(name = "id") String memberId) {
        Map<String, Object> map = new HashMap<>();
        try {
            // checkMemberId메소드를 호출하여 아이디가 존재하는지 확인한다.(있을 경우 1, 없을 경우 0 리턴)
            int result = mService.checkMemberId(memberId);

            // 중복되는 아이디가 존재할 경우
            if (result == 1) {
                map.put("result", Long.valueOf(result));
                map.put("data", "이미 사용중인 아이디입니다.");
            }
            // 중복되는 아이디가 없는 경우
            else {
                map.put("result", Long.valueOf(result));
                map.put("data", "사용가능한 아이디입니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "이미 사용중인 아이디입니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 닉네임 중복확인
    // GET > http://localhost:9090/REST/api/member/checknickname?nickname=회원닉네임
    @RequestMapping(value = "/checknickname", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberCheckNicknameGet(@RequestParam(name = "nickname") String memberNickname) {
        Map<String, Object> map = new HashMap<>();
        try {
            // checkMemberNickname메소드를 호출하여 닉네임이 존재하는지 확인한다.(있을 경우 1, 없을 경우 0 리턴)
            int result = mService.checkMemberId(memberNickname);

            // 중복되는 아이디가 존재할 경우
            if (result == 1) {
                map.put("result", Long.valueOf(result));
                map.put("data", "이미 사용중인 닉네임입니다.");
            }
            // 중복되는 아이디가 없는 경우
            else {
                map.put("result", Long.valueOf(result));
                map.put("data", "사용가능한 닉네임입니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "이미 사용중인 닉네임입니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 비밀번호 변경
    // PUT > http://localhost:9090/REST/api/member/updatepw
    @RequestMapping(value = "/updatepw", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberUpdatePwPost(@RequestBody Map<String, Object> object,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String memberId = (String) object.get("id");
            String memberPw = (String) object.get("password");
            String memberNewPw = (String) object.get("newPassword");

            // 비밀번호 암호화
            BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();

            // 로그인한 사용자의 아이디와 입력받은 사용자의 아이디가 일치하는 경우
            if (jwtUtil.extractUsername(token).equals(memberId)) {
                // 아이디를 이용하여 정보를 가져오기
                Member member = mService.getMember(memberId);

                // DB에 저장된 암호와 입력한 암호가 동일하면 새로운 암호로 변경
                if (bcpe.matches(memberPw, member.getPassword())) {
                    // 새로운 비밀번호 암호화 수행
                    member.setPassword(bcpe.encode(memberNewPw));

                    // 결과 값 저장
                    int result = mService.updatePassword(member);
                    map.put("result", Long.valueOf(result));
                    map.put("data", "비밀번호 변경을 성공했습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "비밀번호 변경을 실패했습니다.");
                }
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("status", 0L);
        }
        // 결과 값 리턴
        return map;
    }

    // 로그인 (아이디, 비밀번호), 로그아웃의 경우 세션스토리지의 토큰 값을 제거한다.
    // POST > http://localhost:9090/REST/api/member/login
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberLoginPost(@RequestBody Member member) {
        Map<String, Object> map = new HashMap<>();
        try {
            // member객체의 아이디와 비밀번호를 넣어 인증을 받는다.
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(member.getId(), member.getPassword()));
            map.put("result", 1L);
            map.put("data", "로그인을 성공했습니다.");
            // member객체의 아이디를 키로하여, 토큰 값을 생성한다.
            map.put("token", jwtUtil.generateToken(member.getId()));
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "로그인을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }
}
