package com.example.oj.api;

import com.example.oj.constant.Utils;
import com.example.oj.document.SubmissionDocument;
import com.example.oj.dto.Submission;
import com.example.oj.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/get-submission-of-problem/{problemId}")
    public ResponseEntity<List<SubmissionDocument>> getSubmissionOfProblem(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String problemId) {
        String email = utils.getEmailFromToken(authorizationHeader);
        List<SubmissionDocument> submissionDocumentList =submissionService.getSubmissionOfProblem(problemId, email);
        Collections.reverse(submissionDocumentList);
        return ResponseEntity.ok(submissionDocumentList);
    }

    @GetMapping("/get-by-problemId/{problemId}")
    public ResponseEntity<List<SubmissionDocument>> getSubmissionOfProblem(@PathVariable String problemId) {
        List<SubmissionDocument> submissionDocumentList = submissionService.getSubmissionByProblem(problemId);
        Collections.reverse(submissionDocumentList);
        return ResponseEntity.ok(submissionDocumentList);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<SubmissionDocument>> getAll() {
        return ResponseEntity.ok(submissionService.getAll());
    }
}
