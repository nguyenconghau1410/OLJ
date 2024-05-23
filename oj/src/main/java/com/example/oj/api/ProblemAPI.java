package com.example.oj.api;

import com.example.oj.constant.Utils;
import com.example.oj.document.ProblemDocument;
import com.example.oj.document.TopicDocument;
import com.example.oj.document.TopicProblemDocument;
import com.example.oj.document.UserDocument;
import com.example.oj.dto.ProblemSmall;
import com.example.oj.dto.TaskContest;
import com.example.oj.dto.TopicProblem;
import com.example.oj.service.ProblemService;
import com.example.oj.service.TopicService;
import com.example.oj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/problem")
@RequiredArgsConstructor
public class ProblemAPI {
    private final ProblemService problemService;
    private final TopicService topicService;
    private final Utils utils;
    @PostMapping("/add")
    public void insert
            (@RequestHeader("Authorization") String authorizationHeader,
             @RequestBody TopicProblem topicProblem
             ) {
        ProblemDocument problemDocument = topicProblem.getProblem();
        List<TopicDocument> topics = topicProblem.getTopics();
        String email = utils.getEmailFromToken(authorizationHeader);
        if(email != null) {
            problemDocument.setEmail(email);
        }
        problemDocument.setState("PRIVATE");
        ProblemDocument problem = problemService.insert(problemDocument);
        for(var x : topics) {
            TopicProblemDocument topicProblemDocument = new TopicProblemDocument();
            topicProblemDocument.setProblemId(problem.getId());
            topicProblemDocument.setTopicId(x.getId());
            topicService.insert(topicProblemDocument);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ProblemDocument>> getAllProblem() {
        return ResponseEntity.ok(problemService.findAll());
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<ProblemDocument> getOne(@PathVariable String id) {
        Optional<ProblemDocument> problemDocument = problemService.findOne(id);
        if(problemDocument.isPresent()) {
            return ResponseEntity.ok(problemDocument.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/update-state/{id}")
    public void update(@PathVariable String id) {
        problemService.update(id);
    }

    @PutMapping("/update")
    public void updateProblem(@RequestBody TopicProblem topicProblem) {
        problemService.update(topicProblem);
    }

    @GetMapping("/get-by-creator")
    public ResponseEntity<List<ProblemSmall>> getProblemByCreator(@RequestHeader("Authorization") String authorizationHeader) {
        String email = utils.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(problemService.findByCreator(email));
    }

    @GetMapping("/get-by-keyword/{keyword}")
    public ResponseEntity<List<ProblemSmall>> findByKeyword(@PathVariable String keyword) {
        return ResponseEntity.ok(problemService.findByKeyword(keyword));
    }

}
