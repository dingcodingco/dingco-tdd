package com.dingcolabs.dingcotdd.polls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QuestionIndexViewTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionRepository questionRepository;

    private Question createQuestion(String questionText, Duration timedeltaFromNow) {
        LocalDateTime time = LocalDateTime.now().plus(timedeltaFromNow);
        return questionRepository.save(new Question(questionText, time));
    }

    @Test
    void test_no_questions() throws Exception {
        // If no questions exist, an appropriate message is displayed.
        mockMvc.perform(get("/polls/"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("등록된 설문조사가 없습니다.")))
            .andExpect(model().attribute("questionList", hasSize(0)));
    }

    @Test
    void test_published_recently_has_new_mark() throws Exception {
        // Questions with a recent pub_date are displayed with a [New] mark.
        createQuestion("Recent question.", Duration.ofHours(-8));
        mockMvc.perform(get("/polls/"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("[New]")))
            .andExpect(model().attribute("questionList", hasSize(1)));
    }

    @Test
    void test_past_question() throws Exception {
        // Questions with a pub_date in the past are displayed on the index page.
        createQuestion("Past question.", Duration.ofDays(-30));
        mockMvc.perform(get("/polls/"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("questionList", hasSize(1)));
    }

    @Test
    void test_future_question() throws Exception {
        // Questions with a pub_date in the future aren't displayed on the index page.
        createQuestion("Future question.", Duration.ofDays(30));
        mockMvc.perform(get("/polls/"))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("등록된 설문조사가 없습니다.")))
            .andExpect(model().attribute("questionList", hasSize(0)));
    }

    @Test
    void test_two_past_questions() throws Exception {
        // The index page may display multiple questions ordered by pub_date desc.
        createQuestion("Past question 1.", Duration.ofDays(-30));
        createQuestion("Past question 2.", Duration.ofDays(-5));

        MvcResult result = mockMvc.perform(get("/polls/"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("questionList", hasSize(2)))
            .andReturn();

        String body = result.getResponse().getContentAsString();
        assertTrue(body.indexOf("Past question 2.") < body.indexOf("Past question 1."),
            "Newest question must appear before the older one");
    }

    @Test
    void test_has_a_href_link() throws Exception {
        // The index page provides a href link to each question's detail page.
        Question question = createQuestion("Recent question.", Duration.ofDays(-30));
        mockMvc.perform(get("/polls/"))
            .andExpect(status().isOk())
            .andExpect(content().string(
                org.hamcrest.Matchers.containsString("href=\"/polls/" + question.getId() + "/\"")));
    }
}
