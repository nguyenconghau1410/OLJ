package com.example.oj.service;

import com.example.oj.document.ProblemDocument;

import com.example.oj.document.TopicDocument;
import com.example.oj.document.TopicProblemDocument;
import com.example.oj.dto.ProblemSmall;
import com.example.oj.dto.TopicProblem;
import com.example.oj.repository.ProblemRepository;
import com.example.oj.repository.TopicProblemRepository;
import com.example.oj.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final TopicProblemRepository topicProblemRepository;
    private final TopicRepository topicRepository;
    public ProblemDocument insert(ProblemDocument problemDocument) {
        return problemRepository.insert(problemDocument);
    }

    public List<ProblemDocument> findAll() {
        return problemRepository.findAllByState("PUBLIC");
    }

    public Optional<ProblemDocument> findOne(String id) {
        return problemRepository.findById(id);
    }

    public List<ProblemSmall> findByCreator(String email) {
        List<ProblemSmall> problemSmallList = new ArrayList<>();
        List<ProblemDocument> problems = problemRepository.findAllByEmail(email);
        for (ProblemDocument problem : problems) {
            problemSmallList.add(new ProblemSmall().convert(problem));
        }
        return problemSmallList;
    }

    public void update(String id) {
        ProblemDocument problemDocument = problemRepository.findById(id).get();
        problemDocument.setState("PUBLIC");
        problemRepository.save(problemDocument);
    }

    public void update(TopicProblem topicProblem) {
        ProblemDocument problem = topicProblem.getProblem();
        problemRepository.save(problem);
        topicProblemRepository.deleteByProblemId(problem.getId());
        List<TopicDocument> topics = topicProblem.getTopics();
        for(var x : topics) {
            TopicProblemDocument topicProblemDocument = new TopicProblemDocument();
            topicProblemDocument.setProblemId(problem.getId());
            topicProblemDocument.setTopicId(x.getId());
            topicProblemRepository.insert(topicProblemDocument);
        }
    }

    public List<ProblemSmall> findByKeyword(String keyword) {
        List<ProblemDocument> list = problemRepository.findByTitleContaining(keyword);
        List<ProblemSmall> problems = new ArrayList<>();
        for (ProblemDocument li: list) {
            problems.add(new ProblemSmall().convert(li));
        }
        return problems;
    }
}
