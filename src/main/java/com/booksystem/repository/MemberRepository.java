package com.booksystem.repository;

import javax.transaction.Transactional;

import com.booksystem.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, String> {

    // 회원가입
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO MEMBER(ID, PASSWORD, NAME, NICKNAME, PHONE, ZIPCODE, ADDRESS, REGDATE, IMAGE, IMAGESIZE, IMAGETYPE) VALUES(:#{#member.id}, :#{#member.password}, :#{#member.name}, :#{#member.nickname}, :#{#member.phone}, :#{#member.zipcode}, :#{#member.address}, CURRENT_TIMESTAMP, :#{#member.image}, :#{#member.imagesize}, :#{#member.imagetype})", nativeQuery = true)
    public int queryJoinMember(@Param("member") Member member);

    // 회원탈퇴
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM MEMBER WHERE ID=:memberId", nativeQuery = true)
    public int queryDeleteMember(@Param("memberId") String memberId);

    // 회원정보수정
    @Transactional
    @Modifying
    @Query(value = "UPDATE MEMBER SET NICKNAME=:#{#member.nickname}, PHONE=:#{#member.phone}, ZIPCODE=:#{#member.zipcode}, ADDRESS=:#{#member.address} WHERE ID=:#{#member.id}", nativeQuery = true)
    public int queryUpdateMember(@Param("member") Member member);

    // 회원정보수정(이미지포함)
    @Transactional
    @Modifying
    @Query(value = "UPDATE MEMBER SET NICKNAME=:#{#member.nickname}, PHONE=:#{#member.phone}, ZIPCODE=:#{#member.zipcode}, ADDRESS=:#{#member.address}, IMAGE=:#{#member.image}, IMAGESIZE=:#{#member.imagesize}, IMAGETYPE=:#{#member.imagetype}WHERE ID=:#{#member.id}", nativeQuery = true)
    public int queryUpdateMemberWithImage(@Param("member") Member member);

    // 비밀번호변경
    @Transactional
    @Modifying
    @Query(value = "UPDATE MEMBER SET PASSWORD=:#{#member.password} WHERE ID=:#{#member.id}", nativeQuery = true)
    public int queryUpdatePassword(@Param("member") Member member);

    // 아이디 중복확인
    @Query(value = "SELECT COUNT(*) FROM MEMBER WHERE ID=:memberId", nativeQuery = true)
    public int queryCheckId(@Param("memberId") String memberId);

    // 닉네임 중복확인
    @Query(value = "SELECT COUNT(*) FROM MEMBER WHERE NICKNAME=:memberNickname", nativeQuery = true)
    public int queryCheckNickname(@Param("memberNickname") String memberNickname);

    // 이전에 사용하던 비밀번호인지 체크
    @Query(value = "SELECT PASSWORD FROM MEMBER WHERE ID=:memberid", nativeQuery = true)
    public String queryCheckPassword(@Param("memberid") String memberid);
}
