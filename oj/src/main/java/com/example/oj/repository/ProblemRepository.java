package com.example.oj.repository;

import com.example.oj.document.ProblemDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProblemRepository extends MongoRepository<ProblemDocument, String> {
    ProblemDocument insert(ProblemDocument problemDocument);
    List<ProblemDocument> findAllByState(String state);

    List<ProblemDocument> findAllByEmail(String email);
    List<ProblemDocument> findByTitleContaining(String keyword);
}
