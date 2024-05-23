package com.example.oj.repository;

import com.example.oj.document.DetailContest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DetailContestRepository extends MongoRepository<DetailContest, String> {
    DetailContest insert(DetailContest detailContest);
    Optional<DetailContest> findById(String id);
    List<DetailContest> findByContestId(String contestId);
    List<DetailContest> findByContestIdAndUserId(String contestId, String userId);
    List<DetailContest> findByContestIdAndProblemId(String contestId, String problemId);
}
