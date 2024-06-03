package com.example.oj.repository;

import com.example.oj.document.ContestDocument;
import com.example.oj.dto.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContestRepository extends MongoRepository<ContestDocument, String> {
    ContestDocument insert(ContestDocument contestDocument);
    Page<ContestDocument> findByCreatedBy(String email, Pageable pageable);
    Page<ContestDocument> findByStateAndIsFinished(String state, boolean isFinished, Pageable pageable);

    Page<ContestDocument> findByParticipants(Participant email, Pageable pageable);
    Integer countByStateAndIsFinished(String state, boolean isFinished);
    Integer countByCreatedBy(String email);

    Integer countByParticipants(Participant participant);
}
