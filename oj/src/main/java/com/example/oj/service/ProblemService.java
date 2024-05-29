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
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final TopicProblemRepository topicProblemRepository;
    private final TopicService topicService;
    public ProblemDocument insert(ProblemDocument problemDocument) {
        return problemRepository.insert(problemDocument);
    }

    public Integer count() {
        return problemRepository.countByState("PUBLIC");
    }

    public List<ProblemDocument> findAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 12);
        Page<ProblemDocument> pages = problemRepository.findAllByState("PUBLIC", pageable);
        List<ProblemDocument> list = new ArrayList<>();
        list.addAll(pages.getContent());
        return list;
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

    public Map<String, Object> search(Map<String, Object> data) {
        if(!data.get("id").equals("")) {
            return topicService.findByTopicId((String) data.get("id"), (Integer) data.get("pageNumber"));
        }
        else {
            Integer total = problemRepository.countByTitleContaining((String) data.get("search"));
            Pageable pageable = PageRequest.of((Integer) data.get("pageNumber"), 12);
            Page<ProblemDocument> problemDocuments = problemRepository.findByTitleContaining((String) data.get("search"), pageable);
            List<ProblemSmall> problems = new ArrayList<>();
            for (ProblemDocument problemDocument: problemDocuments.getContent()) {
                problems.add(new ProblemSmall().convert(problemDocument));
            }
            Map<String, Object> mp = new HashMap<>();
            mp.put("list", problems);
            mp.put("total", total);
            return mp;
        }
    }
}
