package com.booksystem.service;

import com.booksystem.entity.Member;

import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    // 회원가입
    public int joinMember(Member member);

    // 회원탈퇴
    public int deleteMember(String memberId);

    // 회원정보 수정
    public int updateMember(Member member);

    // 회원정보 수정(이미지포함)
    public int updateMemberWithImage(Member member);

    // 아이디 중복확인
    public int checkMemberId(String memberId);

    // 닉네임 중복확인
    public int checkMemberNickname(String memberNickname);

    // 회원정보 조회
    public Member getMember(String memberId);

    // 비밀번호 중복확인
    public String checkMemberPw(String memberId);

    // 비밀번호 변경
    public int updatePassword(Member member);

    // 비밀번호 찾기
    public boolean findMemberPw(String memberId, String memberName);
}
