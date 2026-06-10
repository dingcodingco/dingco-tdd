package com.dingcolabs.dingcotdd.polls;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByPubDateLessThanEqualOrderByPubDateDesc(LocalDateTime now);

    Optional<Question> findByIdAndPubDateLessThanEqual(Long id, LocalDateTime now);
}
