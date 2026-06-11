package com.dingcolabs.dingcotdd;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

// Functional Test (브라우저 없이 실제 HTTP)
// webEnvironment = RANDOM_PORT 로 실제 내장 톰캣을 띄우고,
// TestRestTemplate 으로 진짜 HTTP 요청을 보내 사용자 관점의 전체 흐름을 검증합니다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FunctionalTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;

    @BeforeEach
    void setUp() {
        // 매 테스트 전에 실제 서버의 설문조사 목록 페이지를 요청해둡니다.
        response = restTemplate.getForEntity("/polls/", String.class);
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    void pollsPageIsServed() {
        // 서버가 떠서 200 OK 로 응답한다.
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void pollsPageShowsEmptyMessage() {
        // 아직 등록된 설문조사가 없으므로 안내 문구가 보인다.
        assertThat(response.getBody()).contains("등록된 설문조사가 없습니다.");
    }
}
