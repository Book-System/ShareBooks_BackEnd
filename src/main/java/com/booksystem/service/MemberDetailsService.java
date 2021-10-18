package com.booksystem.service;

import java.util.Collection;

import com.booksystem.entity.Member;
import com.booksystem.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailsService implements UserDetailsService {
    @Autowired
    MemberRepository mRepository;

    @Override
    public UserDetails loadUserByUsername(String memberid) throws UsernameNotFoundException {
        Member member = mRepository.findById(memberid).get();

        String[] userRoles = { "MEMBER" };
        Collection<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(userRoles);

        User user = new User(member.getId(), member.getPassword(), roles);

        return user;
    }

}
