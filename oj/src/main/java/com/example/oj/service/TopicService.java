package com.example.oj.service;

import com.example.oj.document.ProblemDocument;
import com.example.oj.document.TopicDocument;
import com.example.oj.document.TopicProblemDocument;
import com.example.oj.dto.ProblemSmall;
import com.example.oj.repository.ProblemRepository;
import com.example.oj.repository.TopicProblemRepository;
import com.example.oj.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Map<String, Object> findByTopicId(String topicId, Integer pageNumber) {
        Integer total = topicProblemRepository.countByTopicId(topicId);
        Pageable pageable = PageRequest.of(pageNumber, 12);
        Page<TopicProblemDocument> list = topicProblemRepository.findByTopicId(topicId, pageable);
        List<ProblemSmall> problems = new ArrayList<>();
        for(var x : list.getContent()) {
            Optional<ProblemDocument> problemDocument = problemRepository.findById(x.getProblemId());
            if(problemDocument.isPresent()) {
                problems.add(new ProblemSmall().convert(problemDocument.get()));
            }
        }
        Map<String, Object> mp = new HashMap<>();
        mp.put("list", problems);
        mp.put("total", total);
        return mp;
    }
}
