package com.example.oj.repository;

import com.example.oj.document.ProblemDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProblemRepository extends MongoRepository<ProblemDocument, String> {
    ProblemDocument insert(ProblemDocument problemDocument);
    Integer countByState(String state);
    Page<ProblemDocument> findAllByState(String state, Pageable pageable);

    Page<ProblemDocument> findAllByEmail(String email, Pageable pageable);

    Page<ProblemDocument> findAll(Pageable pageable);
    Integer countByEmail(String email);
    List<ProblemDocument> findByTitleContaining(String keyword);
    Page<ProblemDocument> findByTitleContaining(String keyword, Pageable pageable);
    Integer countByTitleContaining(String keyword);
}
