package com.dingcolabs.dingcotdd.polls;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionModelTest {

    @Test
    void wasPublishedRecently_returnsFalse_forFutureQuestion() {
        LocalDateTime time = LocalDateTime.now().plusDays(30);
        Question futureQuestion = new Question("future", time);
        assertEquals(false, futureQuestion.wasPublishedRecently());
    }

    @Test
    void wasPublishedRecently_returnsFalse_forOldQuestion() {
        LocalDateTime time = LocalDateTime.now().minusDays(1).minusSeconds(1);
        Question oldQuestion = new Question("old", time);
        assertEquals(false, oldQuestion.wasPublishedRecently());
    }

    @Test
    void wasPublishedRecently_returnsTrue_forRecentQuestion() {
        LocalDateTime time = LocalDateTime.now().minusHours(23).minusMinutes(59).minusSeconds(59);
        Question recentQuestion = new Question("recent", time);
        assertEquals(true, recentQuestion.wasPublishedRecently());
    }
}
