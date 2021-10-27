package com.booksystem.service;

import java.util.Date;

import com.booksystem.entity.Mail;
import org.springframework.stereotype.Service;

@Service
public interface SendEmailService {

    // 메일을 생성하고 이메일 인증코드를 설정
    public Mail createMailAndConfirmEmail(String memberId);

    // 메일을 생성하고 비밀번호를 변경
    public Mail createMailAndChangePassword(String memberId, String memberName);

    // 비밀번호 갱신
    public void updatePassword(String memberId, String memberPw);

    // 이메일 인증코드 발급
    public String getTempEmailCode();

    // 임시 비밀번호 발급
    public String getTempPassword();

    // 메일 보내기
    public void mailSend(Mail mail);

    // 이메일 인증
    public int validEmailCode(String memberId, String emailCode, Date curDate);
}
