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
class QuestionResultViewTests {

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
        // The result view of a future question returns a 404 not found.
        Question futureQuestion = createQuestion("Future question.", Duration.ofDays(5));
        mockMvc.perform(get("/polls/" + futureQuestion.getId() + "/results/"))
            .andExpect(status().isNotFound());
    }

    @Test
    void test_past_question() throws Exception {
        // The result view of a past question displays the question's text.
        Question pastQuestion = createQuestion("Past question.", Duration.ofDays(-5));
        mockMvc.perform(get("/polls/" + pastQuestion.getId() + "/results/"))
            .andExpect(content().string(containsString(pastQuestion.getQuestionText())));
    }

    @Test
    void test_past_question_with_choices() throws Exception {
        // The result view of a past question displays the question's vote results.
        Question pastQuestion = createQuestion("Past question.", Duration.ofDays(-5));
        Choice choice1 = choiceRepository.save(new Choice(pastQuestion, "choice 1"));
        choiceRepository.save(new Choice(pastQuestion, "choice 2"));

        mockMvc.perform(get("/polls/" + pastQuestion.getId() + "/results/"))
            .andExpect(content().string(containsString("Choice: " + choice1.getChoiceText())))
            .andExpect(content().string(containsString("Vote Count: " + choice1.getVotes())));
    }
}
