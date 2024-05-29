package com.example.oj.service;

import com.example.oj.document.ContestDocument;
import com.example.oj.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;

    public ContestDocument insert(ContestDocument contestDocument) {
        contestDocument.setState("PRIVATE");
        return contestRepository.insert(contestDocument);
    }

    public List<ContestDocument> getOne(String email) {
        return contestRepository.findByCreatedBy(email);
    }

    public ContestDocument findOne(String id) {
        return contestRepository.findById(id).get();
    }

    public void update(ContestDocument contestDocument) {
        contestRepository.save(contestDocument);
    }

    public List<ContestDocument> getContestList() {
        return contestRepository.findByState("PUBLIC");
    }
}
