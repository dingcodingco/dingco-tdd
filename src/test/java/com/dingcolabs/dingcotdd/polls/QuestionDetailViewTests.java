package com.dingcolabs.dingcotdd.polls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QuestionDetailViewTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    private Question createQuestion(String questionText, Duration timedeltaFromNow) {
        LocalDateTime time = LocalDateTime.now().plus(timedeltaFromNow);
        return questionRepository.save(new Question(questionText, time));
    }

    @Test
    void test_future_question() throws Exception {
        // The detail view of a future question returns a 404 not found.
        Question futureQuestion = createQuestion("Future question.", Duration.ofDays(5));
        mockMvc.perform(get("/polls/" + futureQuestion.getId() + "/"))
            .andExpect(status().isNotFound());
    }

    @Test
    void test_past_question() throws Exception {
        // The detail view of a past question displays the question's text.
        Question pastQuestion = createQuestion("Past question.", Duration.ofDays(-5));
        mockMvc.perform(get("/polls/" + pastQuestion.getId() + "/"))
            .andExpect(content().string(containsString(pastQuestion.getQuestionText())));
    }

    @Test
    void test_past_question_with_choices() throws Exception {
        // The detail view of a past question displays the question's choices.
        Question pastQuestion = createQuestion("Past question.", Duration.ofDays(-5));
        choiceRepository.save(new Choice(pastQuestion, "choice 1"));
        choiceRepository.save(new Choice(pastQuestion, "choice 2"));

        mockMvc.perform(get("/polls/" + pastQuestion.getId() + "/"))
            .andExpect(content().string(containsString("choice 1")))
            .andExpect(content().string(containsString("choice 2")));
    }

    @Test
    void test_has_a_href_link() throws Exception {
        // The detail page provides a href link to the results page.
        Question question = createQuestion("Recent question.", Duration.ofDays(-30));
        mockMvc.perform(get("/polls/" + question.getId() + "/"))
            .andExpect(status().isOk())
            .andExpect(content().string(
                containsString("href=\"/polls/" + question.getId() + "/results/\"")));
    }
}
