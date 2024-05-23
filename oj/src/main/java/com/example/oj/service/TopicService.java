package com.example.oj.service;

import com.example.oj.document.ProblemDocument;
import com.example.oj.document.TopicDocument;
import com.example.oj.document.TopicProblemDocument;
import com.example.oj.repository.ProblemRepository;
import com.example.oj.repository.TopicProblemRepository;
import com.example.oj.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final TopicProblemRepository topicProblemRepository;
    private final ProblemRepository problemRepository;
    public TopicDocument insert(TopicDocument topicDocument) {
        return topicRepository.insert(topicDocument);
    }
    public List<TopicDocument> findAll() {
        return topicRepository.findAll();
    }

    public TopicProblemDocument insert(TopicProblemDocument topicProblemDocument) {
        return topicProblemRepository.insert(topicProblemDocument);
    }

    public List<TopicDocument> findByProblemId(String problemId) {
        List<TopicProblemDocument> list = topicProblemRepository.findByProblemId(problemId);
        List<TopicDocument> topics = new ArrayList<>();
        for(var x : list) {
            topics.add(topicRepository.findById(x.getTopicId()).get());
        }
        return topics;
    }

    public List<ProblemDocument> findByTopicId(String topicId) {
        List<TopicProblemDocument> list = topicProblemRepository.findByTopicId(topicId);
        List<ProblemDocument> problems = new ArrayList<>();
        for(var x : list) {
            problems.add(problemRepository.findById(x.getProblemId()).get());
        }
        return problems;
    }
}
