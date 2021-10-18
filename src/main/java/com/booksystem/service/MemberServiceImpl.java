package com.booksystem.service;

import com.booksystem.entity.Member;
import com.booksystem.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberRepository mRepository;

    @Override
    public void joinMember(Member member) {
        mRepository.save(member);
    }
}
