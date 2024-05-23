package com.example.oj.repository;

import com.example.oj.document.SubmissionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubmissionRepository extends MongoRepository<SubmissionDocument, String> {
    SubmissionDocument insert(SubmissionDocument submissionDocument);
    List<SubmissionDocument> findByProblemIdAndUserId(String problemId, String userId);
    List<SubmissionDocument> findByProblemId(String problemId);
}
