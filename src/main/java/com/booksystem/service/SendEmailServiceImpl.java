package com.booksystem.service;

import java.util.Calendar;
import java.util.Date;

import com.booksystem.entity.Mail;
import com.booksystem.entity.Member;
import com.booksystem.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    MemberRepository mRepository;

    private JavaMailSender jMailSender;
    private static final String FROM_ADDRESS = "admin@gmail.com";

    @Override
    public Mail createMailAndConfirmEmail(String memberId) {
        // getTempEmailCode메소드를 호출하여, 이메일 인증코드를 저장한다.
        String tempEmailCode = getTempEmailCode();

        // Mail객체를 생성하고, 속성을 설정한다.
        Mail mail = new Mail();
        mail.setAddress(memberId);
        mail.setTitle("이메일 인증코드 발송입니다. 확인해주세요.");
        mail.setMessage("안녕하세요. 이메일을 검증하기 위한 인증코드입니다. 3분이내에 등록해주세요." + "[" + tempEmailCode + "]");

        // 이전에 이메일 코드를 보낸 사용자인지 확인(1일 경우 수정, 0일경우 등록)
        int result = mRepository.queryCheckEmailCode(memberId);

        // 현재시간을 기준으로 3분이후 시간 설정
        Date curDate = new Date();
        Calendar calendar = Calendar.getInstance();

        // 현재 시간을 설정하고, 3분을 추가한 후 변수에 넣기
        calendar.setTime(curDate);
        calendar.add(Calendar.MINUTE, 3);
        Date validDate = calendar.getTime();
        System.out.println(validDate);

        if (result == 1) {
            // queryUpdateEmailCode메소드 호출(인증코드, 만료시간을 DB에 업데이트한다.)
            mRepository.queryUpdateEmailCode(memberId, tempEmailCode, validDate);
        } else {
            // querySaveEmailCode메소드 호출(회원 아이디, 인증코드, 만료시간을 DB에 저장한다.)
            mRepository.querySaveEmailCode(memberId, tempEmailCode, validDate);
        }
        return mail;
    }

    @Override
    public Mail createMailAndChangePassword(String memberId, String memberName) {
        // getTempPassword메소드를 호출하여, 임시 비밀번호를 저장한다.
        String tempPw = getTempPassword();

        // Mail객체를 생성하고, 속성을 설정한다.
        Mail mail = new Mail();
        mail.setAddress(memberId);
        mail.setTitle(memberName + "님의 임시비밀번호 안내 이메일 입니다.");
        mail.setMessage("안녕하세요. 임시비밀번호 안내 이메일 입니다." + "[" + memberName + "]" + "님의 임시 비밀번호는 " + tempPw + " 입니다.");

        // updatePassword메소드 호출
        updatePassword(memberId, tempPw);
        return mail;
    }

    @Override
    public void updatePassword(String memberId, String memberPw) {
        // 비밀번호 암호화
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        String hashMemberPw = bcpe.encode(memberPw);

        // 아이디를 조건으로 맴버객체 조회
        Member member = mRepository.findById(memberId).get();
        mRepository.queryUpdatePW(member.getId(), hashMemberPw);
    }

    @Override
    public String getTempEmailCode() {
        // 이메일 인증코드 생성을 위한 숫자 배열
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        // 배열을 접근하기 위한 인덱스, 이메일 인증코드를 저장하기 위한 변수
        int index = 0;
        String tempEmailCode = "";

        // 배열의 랜덤한 요소를 뽑아 4자리 비밀번호를 생성
        for (int i = 0; i < 4; i++) {
            index = (int) (charSet.length * Math.random());
            tempEmailCode += charSet[index];
        }
        // 결과 값 리턴
        return tempEmailCode;
    }

    @Override
    public String getTempPassword() {
        // 임시 비밀번호를 위한 문자, 숫자 배열
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        // 배열을 접근하기 위한 인덱스, 임시 비밀번호를 저장하기 위한 변수
        int index = 0;
        String tempPw = "";

        // 배열의 랜덤한 요소를 뽑아 10자리 비밀번호를 생성
        for (int i = 0; i < 10; i++) {
            index = (int) (charSet.length * Math.random());
            tempPw += charSet[index];
        }
        // 결과 값 리턴
        return tempPw;
    }

    @Override
    public void mailSend(Mail mail) {
        // 메세지를 보내기위한 객체 생성 및 설정
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getAddress());
        message.setFrom(SendEmailServiceImpl.FROM_ADDRESS);
        message.setSubject(mail.getTitle());
        message.setText(mail.getMessage());

        // 메일 전송
        jMailSender.send(message);
    }

    @Override
    public int validEmailCode(String memberId, String emailCode, Date curDate) {
        int result = mRepository.queryValidEmailCode(memberId, emailCode, curDate);
        return result;
    }
}