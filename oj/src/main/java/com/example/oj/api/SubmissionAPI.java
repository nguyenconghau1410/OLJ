package com.example.oj.api;

import com.example.oj.constant.Utils;
import com.example.oj.document.SubmissionDocument;
import com.example.oj.dto.Statistic;
import com.example.oj.dto.StatisticContest;
import com.example.oj.dto.Submission;
import com.example.oj.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/submission")
@RequiredArgsConstructor
public class SubmissionAPI {
    private final SubmissionService submissionService;
    private final Utils utils;
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> insert(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Submission submission) {
        String email = utils.getEmailFromToken(authorizationHeader);
        SubmissionDocument submissionDocument = submissionService.insert(submission, email);
        Map<String, String> mp = new HashMap<>();
        mp.put("id", submissionDocument.getId());
        return ResponseEntity.ok(mp);
    }

    @GetMapping("/result/{id}")
    public ResponseEntity<SubmissionDocument> execute(@PathVariable String id) {
        Optional<SubmissionDocument> submissionDocument = submissionService.execute(id);
        if(submissionDocument.isPresent()) {
            return ResponseEntity.ok(submissionDocument.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/get-submission-of-problem/{problemId}/{pageNumber}")
    public ResponseEntity<List<SubmissionDocument>> getSubmissionOfProblem(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String problemId, @PathVariable Integer pageNumber) {
        String email = utils.getEmailFromToken(authorizationHeader);
        List<SubmissionDocument> submissionDocumentList =submissionService.getSubmissionOfProblem(problemId, email, pageNumber);
        return ResponseEntity.ok(submissionDocumentList);
    }

    @GetMapping("/count-submissions-problem/{problemId}/{type}")
    public ResponseEntity<Map<String, Integer>> countSubmissionProblem(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String problemId, @PathVariable String type) {
        Map<String, Integer> mp = new HashMap<>();
        String email = utils.getEmailFromToken(authorizationHeader);
        mp.put("total", submissionService.countSubmissionProblem(problemId, email, type));
        return ResponseEntity.ok(mp);
    }

    @GetMapping("/get-my-submission/{pageNumber}")
    public ResponseEntity<List<SubmissionDocument>> getMySubmission(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer pageNumber) {
        String email = utils.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(submissionService.getSubmissionByUserId(email, pageNumber));
    }

    @GetMapping("/count-my-submission")
    public ResponseEntity<Map<String, Integer>> countMySubmission(@RequestHeader("Authorization") String authorizationHeader) {
        String email = utils.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(submissionService.countMySubmission(email));
    }

    @GetMapping("/get-by-problemId/{problemId}/{pageNumber}")
    public ResponseEntity<List<SubmissionDocument>> getSubmissionOfProblem(@PathVariable String problemId, @PathVariable Integer pageNumber) {
        List<SubmissionDocument> submissionDocumentList = submissionService.getSubmissionByProblem(problemId, pageNumber);
        return ResponseEntity.ok(submissionDocumentList);
    }

    @GetMapping("/get-all/{pageNumber}")
    public ResponseEntity<List<SubmissionDocument>> getAll(@PathVariable Integer pageNumber) {
        return ResponseEntity.ok(submissionService.getAll(pageNumber));
    }

    @GetMapping("/count-total-submissions")
    public ResponseEntity<Map<String, Integer>> count() {
        Map<String, Integer> mp = new HashMap<>();
        mp.put("total", submissionService.count());
        return ResponseEntity.ok(mp);
    }

    @GetMapping("/get-statistic")
    public ResponseEntity<List<Statistic>> getStatistic() {
        return ResponseEntity.ok(submissionService.getStatistic());
    }

    @GetMapping("/get-figure")
    public ResponseEntity<Map<String, Long>> getFigure(@RequestHeader("Authorization") String authorizationHeader) {
        String email = utils.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(submissionService.getFigure(email));
    }
}
