package com.dingcolabs.dingcotdd.polls;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Seeds demo data so the rendered pages have content when running with
 * {@code --spring.profiles.active=demo}. Guarded by the "demo" profile so it
 * never runs during tests.
 */
@Profile("demo")
@Component
public class DataSeeder implements CommandLineRunner {

    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;

    public DataSeeder(QuestionRepository questionRepository, ChoiceRepository choiceRepository) {
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
    }

    @Override
    public void run(String... args) {
        if (questionRepository.count() > 0) {
            return;
        }

        Question recent = questionRepository.save(
            new Question("What's up?", LocalDateTime.now().minusHours(8)));
        choiceRepository.save(new Choice(recent, "choice!"));
        choiceRepository.save(new Choice(recent, "choice 2!"));

        questionRepository.save(
            new Question("Past question 1.", LocalDateTime.now().minusDays(30)));
        questionRepository.save(
            new Question("Past question 2.", LocalDateTime.now().minusDays(5)));
    }
}
