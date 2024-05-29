package com.example.oj.repository;

import com.example.oj.document.DetailContest;
import com.example.oj.dto.DetailLeaderboard;
import com.example.oj.dto.LeaderBoard;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DetailContestRepository extends MongoRepository<DetailContest, String> {
    DetailContest insert(DetailContest detailContest);
    Optional<DetailContest> findById(String id);
    List<DetailContest> findByContestId(String contestId);
    List<DetailContest> findByContestIdAndUserId(String contestId, String userId);
    List<DetailContest> findByContestIdAndProblemId(String contestId, String problemId);

    @Aggregation(pipeline = {
            "{ $match: { contestId: ?0 } }",
            "{ $addFields: { parsedDate: { $dateFromString: { dateString: { $substr: ['$createdAt', 0, 23] } } } } }",
            "{ $sort: { problemId: 1, point: -1, createAt: 1, time: 1, memory: 1 } }",
            "{ $group: { _id: { problemId: '$problemId', userId: '$userId' }, document: { $first: '$$ROOT' } } }",
            "{ $group: { _id: '$document.userId', name: { $first: '$document.name' }, totalScore: { $sum: '$document.point' }, totalSolutions: { $sum: { $cond: { if: { $ne: ['$document.point', 0] }, then: 1, else: 0 } } }, totalSeconds: { $sum: { $add: [{ $divide: [{ $toLong: '$document.parsedDate' }, 3600000] }] } } } }",
            "{ $sort: { totalScore: -1, totalSolutions: -1, totalSeconds: 1 } }"
    })
    List<LeaderBoard> getLeaderBoard(String contestId);

    @Aggregation(pipeline = {
            "{ $match: { contestId: ?0, userId: ?1 }}",
            "{ $sort: { problemId: 1, point: -1, createAt: 1, time: 1, memory: 1 } }",
            "{ $group: { _id: { problemId: '$problemId' }, document: { $first: '$$ROOT' } } }"
    })
    List<DetailLeaderboard> getDetailLeaderBoard(String contestId, String userId);
}
