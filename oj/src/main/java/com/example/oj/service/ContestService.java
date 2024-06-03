package com.example.oj.service;

import com.example.oj.document.ContestDocument;
import com.example.oj.dto.Participant;
import com.example.oj.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;

    public ContestDocument insert(ContestDocument contestDocument) {
        contestDocument.setState("PRIVATE");
        return contestRepository.insert(contestDocument);
    }

    public List<ContestDocument> getOne(String email, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());
        return contestRepository.findByCreatedBy(email, pageable).getContent();
    }

    public ContestDocument findOne(String id) {
        return contestRepository.findById(id).get();
    }

    public void update(ContestDocument contestDocument) {
        contestRepository.save(contestDocument);
    }

    public List<ContestDocument> getContestList(boolean isFinished, Integer pageNumber) {
        Pageable pageable;
        if(isFinished) {
            pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());
        }
        else {
            pageable = PageRequest.of(pageNumber, 10, Sort.by("startTime"));
        }
        Page<ContestDocument> page = contestRepository.findByStateAndIsFinished("PUBLIC", isFinished, pageable);
        if(page != null)
            return page.getContent();
        return new ArrayList<>();
    }

    public Map<String, Integer> countContestList(boolean isFinished) {
        Integer total = contestRepository.countByStateAndIsFinished("PUBLIC", isFinished);
        Map<String, Integer> mp = new HashMap<>();
        mp.put("total", total);
        return mp;
    }

    public Map<String, Integer> countByCreatedBy(String email) {
        Map<String, Integer> mp = new HashMap<>();
        mp.put("total", contestRepository.countByCreatedBy(email));
        return mp;
    }

    public List<ContestDocument> getHistoryContest(String email, Integer pageNumber) {
        Participant participant = new Participant();
        participant.setEmail(email);
        participant.setJoined(true);
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("endTime").descending());
        Page<ContestDocument> page = contestRepository.findByParticipants(participant, pageable);
        if (page != null)
            return page.getContent();
        return new ArrayList<>();
    }

    public Map<String, Integer> countHistoryContest(String email) {
        Participant participant = new Participant();
        participant.setEmail(email);
        participant.setJoined(true);
        Map<String, Integer> mp = new HashMap<>();
        Integer total = contestRepository.countByParticipants(participant);
        if(total == null) {
            total = 0;
        }
        mp.put("total", total);
        return mp;
    }


}
