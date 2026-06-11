package com.dingcolabs.dingcotdd.polls;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// 순수 Mockito (Spring 컨텍스트 없음) — 가장 가벼운 모킹 예제
@ExtendWith(MockitoExtension.class)
class MockitoBasicsTest {

    @Mock
    private QuestionRepository questionRepository;   // 가짜(mock) 객체

    @Test
    void stub_returnsConfiguredValue() {
        // 행동 지정(stub): 이 메서드가 호출되면 이 값을 돌려줘라
        when(questionRepository.findByPubDateLessThanEqualOrderByPubDateDesc(any()))
            .thenReturn(List.of(new Question("좋아하는 언어는?", LocalDateTime.now().minusDays(1))));

        List<Question> result =
            questionRepository.findByPubDateLessThanEqualOrderByPubDateDesc(LocalDateTime.now());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuestionText()).isEqualTo("좋아하는 언어는?");
    }

    @Test
    void verify_checksInteraction() {
        // 호출 검증: 그 메서드가 실제로 호출됐는지 확인
        questionRepository.findById(1L);
        verify(questionRepository, times(1)).findById(1L);
    }
}
