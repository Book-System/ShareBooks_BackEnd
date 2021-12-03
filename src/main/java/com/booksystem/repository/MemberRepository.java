package com.booksystem.repository;

import java.util.Date;

import javax.transaction.Transactional;

import com.booksystem.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
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

        // 회원정보 수정
        @Transactional
        @Modifying
        @Query(value = "UPDATE MEMBER SET NICKNAME=:#{#member.nickname}, PHONE=:#{#member.phone}, ZIPCODE=:#{#member.zipcode}, ADDRESS=:#{#member.address} WHERE ID=:#{#member.id}", nativeQuery = true)
        public int queryUpdateMember(@Param("member") Member member);

        // 회원정보 수정(이미지포함)
        @Transactional
        @Modifying
        @Query(value = "UPDATE MEMBER SET NICKNAME=:#{#member.nickname}, PHONE=:#{#member.phone}, ZIPCODE=:#{#member.zipcode}, ADDRESS=:#{#member.address}, IMAGE=:#{#member.image}, IMAGESIZE=:#{#member.imagesize}, IMAGETYPE=:#{#member.imagetype} WHERE ID=:#{#member.id}", nativeQuery = true)
        public int queryUpdateMemberWithImage(@Param("member") Member member);

        // 비밀번호 변경
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
        @Query(value = "SELECT PASSWORD FROM MEMBER WHERE ID=:memberId", nativeQuery = true)
        public String queryCheckPassword(@Param("memberId") String memberId);

        // 회원정보 조회
        @Query(value = "SELECT * FROM MEMBER WHERE ID=:memberId ", nativeQuery = true)
        public Member queryMemberGet(@Param("memberId") String memberId);

        // 비밀번호 찾기 수행 후 비밀번호 변경
        @Transactional
        @Modifying
        @Query(value = "UPDATE MEMBER SET PASSWORD=:memberPw WHERE ID=:memberId", nativeQuery = true)
        public void queryUpdatePW(@Param("memberId") String memberId, @Param("memberPw") String memberPw);

        // 인증코드 저장하기
        @Transactional
        @Modifying
        @Query(value = "INSERT INTO EMAILCODE(ID, CODE, VALIDDATE) VALUES(:memberId, :emailCode, :validDate)", nativeQuery = true)
        public void querySaveEmailCode(@Param("memberId") String memberId, @Param("emailCode") String emailCode,
                        @Param("validDate") Date validDate);

        // 인증코드 수정하기
        @Transactional
        @Modifying
        @Query(value = "UPDATE EMAILCODE SET CODE=:emailCode, VALIDDATE=:validDate WHERE ID=:memberId", nativeQuery = true)
        public void queryUpdateEmailCode(@Param("memberId") String memberId, @Param("emailCode") String emailCode,
                        @Param("validDate") Date validDate);

        // 인증코드 삭제하기(스케줄러에서 실행)
        @Transactional
        @Modifying
        @Query(value = "DELETE FROM EMAILCODE WHERE ID=:memberId", nativeQuery = true)
        public void queryDeleteEmailCode(@Param("memberId") String memberId);

        // 이전에 인증코드를 받은 회원 조회
        @Query(value = "SELECT COUNT(*) FROM EMAILCODE WHERE ID=:memberId", nativeQuery = true)
        public int queryCheckEmailCode(@Param("memberId") String memberId);

        // 이메일 인증
        @Query(value = "SELECT COUNT(*) FROM EMAILCODE WHERE ID=:memberId AND CODE=:emailCode AND VALIDDATE>=:curDate", nativeQuery = true)
        public int queryValidEmailCode(@Param("memberId") String memberId, @Param("emailCode") String emailCode,
                        @Param("curDate") Date curDate);

        // 닉네임으로 아이디찾기
        @Query(value = "SELECT ID FROM MEMBER WHERE NICKNAME=:nickname", nativeQuery = true)
        public String queryFindId(@Param("nickname") String nickname);
}
