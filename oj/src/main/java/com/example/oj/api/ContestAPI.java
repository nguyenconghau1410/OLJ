package com.example.oj.api;

import com.example.oj.constant.Constant;
import com.example.oj.constant.Utils;
import com.example.oj.document.ContestDocument;
import com.example.oj.document.DetailContest;
import com.example.oj.document.ProblemDocument;
import com.example.oj.document.UserDocument;
import com.example.oj.dto.*;
import com.example.oj.service.ContestService;
import com.example.oj.service.DetailContestService;
import com.example.oj.service.ProblemService;
import com.example.oj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/contest")
@RequiredArgsConstructor
public class ContestAPI {
    private final Utils utils;
    private final ContestService contestService;
    private final ProblemService problemService;
    private final UserService userService;
    private final DetailContestService detailContestService;
    @PostMapping("/add")
    public ResponseEntity<ContestDocument> insert(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ContestDocument contestDocument) {
        String email = utils.getEmailFromToken(authorizationHeader);
        if(email != null) {
            contestDocument.setId(null);
            contestDocument.setCreatedBy(email);
            return ResponseEntity.ok(contestService.insert(contestDocument));
        }
        return null;
    }

    @GetMapping("/get")
    public ResponseEntity<List<ContestDocument>> getOne(@RequestHeader("Authorization") String authorizationHeader) {
        String email = utils.getEmailFromToken(authorizationHeader);
        if(email != null) {
            List<ContestDocument> contestDocumentList = contestService.getOne(email);
            Collections.reverse(contestDocumentList);
            return ResponseEntity.ok(contestDocumentList);
        }
        return null;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ContestDocument> findOne(@PathVariable String id) {
        return ResponseEntity.ok(contestService.findOne(id));
    }

    @PutMapping("/update")
    public void update(@RequestBody ContestDocument contestDocument) {
        contestService.update(contestDocument);
    }

    @GetMapping("/get-challenges/{id}")
    public ResponseEntity<List<ProblemSmall>> getChallenges(@PathVariable String id) {
        ContestDocument contestDocument = contestService.findOne(id);
        List<ProblemSmall> problems = new ArrayList<>();
        for (TaskContest task: contestDocument.getProblems()) {
            ProblemDocument problemDocument = problemService.findOne(task.getId()).get();
            ProblemSmall problemSmall = new ProblemSmall().convert(problemDocument);
            problemSmall.setPoint(task.getPoint());
            problems.add(problemSmall);
        }
        return ResponseEntity.ok(problems);
    }

    @GetMapping("/get-participants/{id}")
    public ResponseEntity<List<UserDocument>> getParticipants(@PathVariable String id) {
        ContestDocument contestDocument = contestService.findOne(id);
        List<UserDocument> participants = new ArrayList<>();
        for (Participant participant : contestDocument.getParticipants()) {
            UserDocument userDocument = userService.findOneByEmail(participant.getEmail());
            participants.add(userDocument);
        }
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/get-signups/{id}")
    public ResponseEntity<List<UserDocument>> getSignUps(@PathVariable String id) {
        ContestDocument contestDocument = contestService.findOne(id);
        List<UserDocument> signups = new ArrayList<>();
        if(contestDocument.getSignups() != null) {
            for (SignUp signUp: contestDocument.getSignups()) {
                UserDocument userDocument = userService.findOneByEmail(signUp.getEmail());
                signups.add(userDocument);
            }
        }

        return ResponseEntity.ok(signups);
    }

    @PostMapping("/add-submission")
    public ResponseEntity<Map<String, String>> addSubmission(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Submission submission) {
        String email = utils.getEmailFromToken(authorizationHeader);
        DetailContest detailContest = detailContestService.insert(submission, email);
        Map<String, String> mp = new HashMap<>();
        mp.put("id", detailContest.getId());
        return ResponseEntity.ok(mp);
    }

    @GetMapping("/get-submission/{id}")
    public ResponseEntity<DetailContest> getSubmission(@PathVariable String id) {
        Optional<DetailContest> detailContest = detailContestService.findById(id);
        if(detailContest.isPresent()) {
            return ResponseEntity.ok(detailContest.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/get-all-submission/{contestId}")
    public ResponseEntity<List<DetailContest>> getAllSubmission(@PathVariable String contestId) {
        List<DetailContest> detailContestList = detailContestService.getAll(contestId);
        Collections.reverse(detailContestList);
        return ResponseEntity.ok(detailContestList);
    }

    @GetMapping("/get-my-submission/{contestId}")
    public ResponseEntity<List<DetailContest>> getMySubmission(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String contestId) {
        String email = utils.getEmailFromToken(authorizationHeader);
        String userId = Constant.getId(email);
        List<DetailContest> detailContestList = detailContestService.getByContestIdAndUserId(contestId, userId);
        Collections.reverse(detailContestList);
        return ResponseEntity.ok(detailContestList);
    }

    @GetMapping("/get-submission-of-problem/{contestId}/{problemId}")
    public ResponseEntity<List<DetailContest>> getSubmissionOfProblem(@PathVariable String contestId, @PathVariable String problemId) {
        List<DetailContest> detailContestList = detailContestService.getByContestIdAndProblemId(contestId, problemId);
        Collections.reverse(detailContestList);
        return ResponseEntity.ok(detailContestList);
    }

    @GetMapping("/get-leader-board/{contestId}")
    public ResponseEntity<List<LeaderBoard>> getLeaderBoard(@PathVariable String contestId) {
        return ResponseEntity.ok(detailContestService.getLeaderBoard(contestId));
    }

    @GetMapping("/get-detail-leaderboard/{contestId}/{userId}")
    public ResponseEntity<List<DetailLeaderboard>> getDetailLeaderboard(@PathVariable String contestId, @PathVariable String userId) {
        return ResponseEntity.ok(detailContestService.getDetailLeaderboard(contestId, userId));
    }

    @GetMapping("/get-contest-list")
    public ResponseEntity<List<ContestDocument>> getContestList() {
        List<ContestDocument> contestDocumentList = contestService.getContestList();
        Collections.reverse(contestDocumentList);
        return ResponseEntity.ok(contestDocumentList);
    }
}
