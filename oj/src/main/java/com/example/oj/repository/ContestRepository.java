package com.example.oj.repository;

import com.example.oj.document.ContestDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContestRepository extends MongoRepository<ContestDocument, String> {
    ContestDocument insert(ContestDocument contestDocument);
    List<ContestDocument> findByCreatedBy(String email);
    List<ContestDocument> findByState(String state);
}
