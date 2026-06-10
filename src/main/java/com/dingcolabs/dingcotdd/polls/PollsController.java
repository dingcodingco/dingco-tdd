 package com.dingcolabs.dingcotdd.polls;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/polls")
public class PollsController {

    private final QuestionRepository questionRepository;

    public PollsController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute(
            "questionList",
            questionRepository.findByPubDateLessThanEqualOrderByPubDateDesc(LocalDateTime.now())
        );
        return "polls/index";
    }

    @GetMapping("/{questionId}/")
    public String detail(@PathVariable Long questionId, Model model) {
        Question question = questionRepository
            .findByIdAndPubDateLessThanEqual(questionId, LocalDateTime.now())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("question", question);
        return "polls/detail";
    }

    @GetMapping("/{questionId}/results/")
    public String result(@PathVariable Long questionId, Model model) {
        Question question = questionRepository
            .findByIdAndPubDateLessThanEqual(questionId, LocalDateTime.now())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("question", question);
        return "polls/result";
    }
}
