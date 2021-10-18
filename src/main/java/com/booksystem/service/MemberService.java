package com.booksystem.service;

import com.booksystem.entity.Member;

import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    // 회원가입
    public void joinMember(Member member);
}
