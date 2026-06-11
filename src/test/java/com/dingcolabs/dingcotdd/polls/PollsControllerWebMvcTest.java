package com.dingcolabs.dingcotdd.polls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

// Integration Test (Web slice)
// @WebMvcTest 는 컨트롤러와 웹 계층(MockMvc)만 로드하고 DB/레포는 로드하지 않습니다.
// 그래서 레포지토리는 @MockBean 으로 가짜 객체를 주입해 동작을 흉내냅니다.
@WebMvcTest(PollsController.class)
class PollsControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionRepository questionRepository;

    @Test
    void indexShowsQuestionText() throws Exception {
        when(questionRepository.findByPubDateLessThanEqualOrderByPubDateDesc(any()))
            .thenReturn(List.of(new Question("좋아하는 언어는?", LocalDateTime.now().minusDays(1))));

        mockMvc.perform(get("/polls/"))
            .andExpect(status().isOk())
            .andExpect(view().name("polls/index"))
            .andExpect(content().string(containsString("좋아하는 언어는?")));
    }

    @Test
    void detailReturns404WhenQuestionMissing() throws Exception {
        when(questionRepository.findByIdAndPubDateLessThanEqual(any(), any()))
            .thenReturn(Optional.empty());

        mockMvc.perform(get("/polls/99/"))
            .andExpect(status().isNotFound());
    }
}
