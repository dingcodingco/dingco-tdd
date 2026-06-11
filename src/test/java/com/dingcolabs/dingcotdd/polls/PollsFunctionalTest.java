package com.dingcolabs.dingcotdd.polls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

// Functional Test (브라우저 없이 실제 HTTP)
// webEnvironment = RANDOM_PORT 로 실제 내장 톰캣을 띄우고,
// TestRestTemplate 으로 진짜 HTTP 요청을 보내 전체 흐름을 검증합니다.
// (Selenium 처럼 브라우저를 띄우지 않아도 되는 가벼운 기능 테스트 방식)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PollsFunctionalTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void pollsIndexIsServedOverHttp() {
        String body = restTemplate.getForObject("/polls/", String.class);
        assertThat(body).contains("등록된 설문조사가 없습니다.");
    }
}
