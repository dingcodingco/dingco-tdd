package com.dingcolabs.dingcotdd.polls;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Question question;

    private String choiceText;

    private int votes = 0;

    protected Choice() {
    }

    public Choice(Question question, String choiceText) {
        this.question = question;
        this.choiceText = choiceText;
        question.getChoices().add(this);
    }

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
