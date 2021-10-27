package com.booksystem.scheduler;

import com.booksystem.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    MemberRepository mRepository;

    // 하루의 끝에 테이블에 있는 모든 인증코드를 삭제한다. => 아직 안함
    @Scheduled(cron = "1 * * * * *")
    public void deleteEmailCode(String memberId) {
        System.out.println("Hello");
    }
}
