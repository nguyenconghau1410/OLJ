package com.example.oj.api;

import com.example.oj.document.TopicDocument;
import com.example.oj.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topic")
@RequiredArgsConstructor
public class TopicAPI {
    private final TopicService topicService;

    @PostMapping("/add")
    public ResponseEntity<TopicDocument> insert(@RequestBody TopicDocument topicDocument) {
        return ResponseEntity.ok(topicService.insert(topicDocument));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<TopicDocument>> findAll() {
        return ResponseEntity.ok(topicService.findAll());
    }

    @GetMapping("/get-topic-of-problem/{id}")
    public ResponseEntity<List<TopicDocument>> getTopic(@PathVariable String id) {
        return ResponseEntity.ok(topicService.findByProblemId(id));
    }
}
