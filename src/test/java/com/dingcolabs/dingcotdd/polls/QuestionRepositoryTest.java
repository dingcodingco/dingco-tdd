package com.dingcolabs.dingcotdd.polls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Integration Test (Repository slice)
// @DataJpaTest 는 JPA 관련 빈과 임베디드 DB(H2)만 로드합니다.
// 전체 컨텍스트를 띄우지 않아 @SpringBootTest 보다 가볍고 빠릅니다.
// 각 테스트는 끝나면 자동으로 롤백됩니다.
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void publishedQuestionsAreReturnedNewestFirst() {
        questionRepository.save(new Question("오래된 질문", LocalDateTime.now().minusDays(10)));
        questionRepository.save(new Question("최근 질문", LocalDateTime.now().minusDays(1)));

        List<Question> result =
            questionRepository.findByPubDateLessThanEqualOrderByPubDateDesc(LocalDateTime.now());

        assertEquals(2, result.size());
        assertEquals("최근 질문", result.get(0).getQuestionText()); // 최신순 정렬
    }

    @Test
    void futureQuestionIsNotFoundById() {
        Question future = questionRepository.save(
            new Question("미래 질문", LocalDateTime.now().plusDays(5)));

        Optional<Question> found =
            questionRepository.findByIdAndPubDateLessThanEqual(future.getId(), LocalDateTime.now());

        assertTrue(found.isEmpty()); // 아직 발행되지 않아 조회되지 않음
    }
}
