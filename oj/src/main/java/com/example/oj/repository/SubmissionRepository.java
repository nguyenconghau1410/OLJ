package com.example.oj.repository;

import com.example.oj.document.SubmissionDocument;
import com.example.oj.dto.Statistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Map;

public interface SubmissionRepository extends MongoRepository<SubmissionDocument, String> {
    SubmissionDocument insert(SubmissionDocument submissionDocument);
//    List<SubmissionDocument> findByProblemIdAndUserId(String problemId, String userId);

    Page<SubmissionDocument> findByProblemIdAndUserId(String problemId, String userId, Pageable pageable);
    Page<SubmissionDocument> findByProblemId(String problemId, Pageable pageable);

    Page<SubmissionDocument> findAll(Pageable pageable);

    Integer countByProblemId(String problemId);
    Integer countByProblemIdAndUserId(String problemId, String userId);

    @Aggregation(pipeline = {
            "{ $group: { _id: '$result', quantity: { $sum: 1 } } }",
            "{ $sort: { _id: 1 } }"
    })
    List<Statistic> getStatistic();
}
