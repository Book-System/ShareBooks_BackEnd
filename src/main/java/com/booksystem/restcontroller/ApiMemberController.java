package com.booksystem.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.booksystem.entity.Mail;
import com.booksystem.entity.Member;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.service.MemberService;
import com.booksystem.service.SendEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    SendEmailService sendEmailService;

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
                // 회원 아이디를 통해 DB에 저장된 객체 추출하기
                String memberId = jwtUtil.extractUsername(token);
                Member tempMember = mService.getMember(memberId);

                // 프로필 사진을 변경할 경우
                if (file.getSize() > 0) {
                    // 회원정보 설정
                    tempMember.setNickname(member.getNickname());
                    tempMember.setPhone(member.getPhone());
                    tempMember.setZipcode(member.getZipcode());
                    tempMember.setAddress(member.getAddress());
                    tempMember.setImage(file.getBytes());
                    tempMember.setImagesize(file.getSize());
                    tempMember.setImagetype(file.getContentType());

                    // updateMemberWithImage메소드 호출
                    int result = mService.updateMemberWithImage(tempMember);
                    map.put("result", Long.valueOf(result));
                    map.put("data", "회원정보수정을 성공했습니다.");
                }
                // 프로필 사진을 변경하지 않을 경우
                else {
                    // 회원정보 설정
                    tempMember.setNickname(member.getNickname());
                    tempMember.setPhone(member.getPhone());
                    tempMember.setZipcode(member.getZipcode());
                    tempMember.setAddress(member.getAddress());

                    // updateMember메소드 호출
                    int result = mService.updateMember(member);
                    map.put("result", Long.valueOf(result));
                    map.put("data", "회원정보수정을 성공했습니다.");
                }
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
                map.put("result", 0L);
                map.put("data", "이미 사용중인 아이디입니다.");
            }
            // 중복되는 아이디가 없는 경우
            else {
                map.put("result", 1L);
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
            int result = mService.checkMemberNickname(memberNickname);

            // 중복되는 아이디가 존재할 경우
            if (result == 1) {
                map.put("result", 0L);
                map.put("data", "이미 사용중인 닉네임입니다.");
            }
            // 중복되는 아이디가 없는 경우
            else {
                map.put("result", 1L);
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

                    // updatePassword메소드 호출
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

    // 프로필사진 조회
    // GET > http://localhost:9090/REST/api/member/image
    @RequestMapping(value = "/image", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> memberImageGet(@RequestHeader("token") String token) throws IOException {
        try {
            // 토큰에 해당하는 아이디를 가져온다.
            String memberId = jwtUtil.extractUsername(token);

            // memberImage메소드를 호출
            Member member = mService.getMember(memberId);

            // 이미지가 있을 경우
            if (member.getImage().length > 0) {
                HttpHeaders headers = new HttpHeaders();
                if (member.getImagetype().equals("image/jpeg")) {
                    headers.setContentType(MediaType.IMAGE_JPEG);
                } else if (member.getImagetype().equals("image/png")) {
                    headers.setContentType(MediaType.IMAGE_PNG);
                } else if (member.getImagetype().equals("image/gif")) {
                    headers.setContentType(MediaType.IMAGE_GIF);
                }
                // 저장된 이미지 출력
                ResponseEntity<byte[]> response = new ResponseEntity<>(member.getImage(), headers, HttpStatus.OK);
                return response;
            } else {
                // 기본 이미지 출력
                InputStream iStream = resourceLoader.getResource("classpath:/static/images/default.png")
                        .getInputStream();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                ResponseEntity<byte[]> response = new ResponseEntity<>(iStream.readAllBytes(), headers, HttpStatus.OK);
                return response;
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();

            // 기본 이미지 출력
            InputStream iStream = resourceLoader.getResource("classpath:/static/images/default.png").getInputStream();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            ResponseEntity<byte[]> response = new ResponseEntity<>(iStream.readAllBytes(), headers, HttpStatus.OK);
            return response;
        }
    }

    // 비밀번호 찾기(아이디, 이름)
    // GET > http://localhost:9090/REST/api/member/findpw
    @RequestMapping(value = "/findpw", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberFindPwGet(@RequestParam String memberId, @RequestParam String memberName) {
        Map<String, Object> map = new HashMap<>();
        try {

            System.out.println(memberId);
            System.out.println(memberName);

            // findMemberPw메서드 호출(true일 경우 통과)
            boolean result = mService.findMemberPw(memberId, memberName);

            if (result == true) {
                map.put("result", 1L);
                map.put("data", "비밀번호 임시번호를 발송합니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "아이디와 이름정보가 일치하는 사용자가 존재하지 않습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "아이디와 이름정보가 일치하는 사용자가 존재하지 않습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 임시 비밀번호 전송
    // POST > http://localhost:9090/REST/api/member/sendemail
    @RequestMapping(value = "/sendemail", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberSendEmailPost(@RequestParam(name = "memberId") String memberId,
            @RequestParam(name = "memberName") String memberName) {
        Map<String, Object> map = new HashMap<>();
        try {
            Mail mail = sendEmailService.createMailAndChangePassword(memberId, memberName);
            sendEmailService.mailSend(mail);
            map.put("result", 1L);
            map.put("data", "임시번호가 발급을 성공했습니다.");
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "임시번호 발급을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 회원가입 시 이메일 인증코드 전송 => 스케줄러도 함께 실행
    // POST > http://localhost:9090/REST/api/member/confirmemail
    @RequestMapping(value = "/confirmemail", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberConfirmEmailPost(@RequestParam(name = "memberId") String memberId) {
        Map<String, Object> map = new HashMap<>();
        try {
            Mail mail = sendEmailService.createMailAndConfirmEmail(memberId);
            sendEmailService.mailSend(mail);
            map.put("result", 1L);
            map.put("data", "이메일 인증코드 발송을 성공했습니다.");
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "이메일 인증코드 발송을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 이메일 인증코드 확인
    // POST > http://localhost:9090/REST/api/member/validemail
    @RequestMapping(value = "/validemail", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberValidEmailPost(@RequestParam(name = "memberId") String memberId,
            @RequestParam(name = "emailCode") String emailCode) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 1일 경우 이메일 인증 완료, 0일 경우 다시 이메일 인증을 수행해야한다.
            int result = sendEmailService.validEmailCode(memberId, emailCode, new Date());
            if (result == 1) {
                map.put("result", 1L);
                map.put("data", "이메일 인증을 완료하였습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "이메일 인증을 실패하였습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "이메일 인증을 실패하였습니다.");
        }
        // 결과 값 리턴
        return map;
    }
}
