package com.booksystem.service;

import java.util.Optional;

import com.booksystem.entity.Member;
import com.booksystem.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberRepository mRepository;

    // 회원가입
    @Override
    public int joinMember(Member member) {
        return mRepository.queryJoinMember(member);
    }

    // 회원탈퇴
    @Override
    public int deleteMember(String memberId) {
        return mRepository.queryDeleteMember(memberId);
    }

    // 회원정보 수정
    @Override
    public int updateMember(Member member) {
        return mRepository.queryUpdateMember(member);
    }

    // 회원정보 수정(이미지포함)
    @Override
    public int updateMemberWithImage(Member member) {
        return mRepository.queryUpdateMemberWithImage(member);
    }

    // 회원정보 조회
    @Override
    public Member getMember(String memberId) {
        // 아이디를 조건으로 검색하여 없을 경우 null을 리턴
        Optional<Member> member = mRepository.findById(memberId);
        return member.orElse(null);
    }

    // 비밀번호 변경
    @Override
    public int updatePassword(Member member) {
        return mRepository.queryUpdatePassword(member);
    }

    // 아이디 중복확인
    @Override
    public int checkMemberId(String memberId) {
        return mRepository.queryCheckId(memberId);
    }

    // 닉네임 중복확인
    @Override
    public int checkMemberNickname(String memberNickname) {
        return mRepository.queryCheckNickname(memberNickname);
    }

    // 비밀번호 중복확인
    @Override
    public String checkMemberPw(String memberId) {
        return null;
    }

    // 비밀번호 찾기
    @Override
    public boolean findMemberPw(String memberId, String memberName) {
        // DB에서 아이디에 해당하는 정보 조회
        Member member = mRepository.findById(memberId).get();

        System.out.println(member.getName());

        // member객체가 null이 아니고, 이름이 입력받은 값과 동일할 경우 true반환
        if (member != null && member.getName().equals(memberName)) {
            return true;
        } else {
            return false;
        }
    }
}