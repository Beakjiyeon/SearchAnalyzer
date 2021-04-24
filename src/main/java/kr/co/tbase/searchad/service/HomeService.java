package kr.co.tbase.searchad.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Sample Service
 */
@Slf4j
@Service
public class HomeService {

    public String getStringCurrentDate() {
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
/*
    public Member memberInfo() {
        Member member = new Member();
        member.setId("example1");
        member.setPassword("example1");

        return member;

    }

 */
}
