package com.dingcolabs.dingcotdd.polls;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String questionText;

    private LocalDateTime pubDate;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Choice> choices = new ArrayList<>();

    protected Question() {
    }

    public Question(String questionText, LocalDateTime pubDate) {
        this.questionText = questionText;
        this.pubDate = pubDate;
    }

    public boolean wasPublishedRecently() {
        LocalDateTime now = LocalDateTime.now();
        return !pubDate.isBefore(now.minusDays(1)) && !pubDate.isAfter(now);
    }
}
