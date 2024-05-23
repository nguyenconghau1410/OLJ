package com.example.oj.service;

import com.example.oj.constant.Constant;
import com.example.oj.constant.Utils;
import com.example.oj.document.SubmissionDocument;
import com.example.oj.document.UserDocument;
import com.example.oj.dto.Submission;
import com.example.oj.repository.SubmissionRepository;
import com.example.oj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final Utils utils;
    public SubmissionDocument insert(Submission submission, String email) {
        UserDocument userDocument = userRepository.findOneByEmail(email);
        SubmissionDocument submissionDocument = new SubmissionDocument();
        submissionDocument.setSource(submission.getCode());
        submissionDocument.setLanguage(submission.getLanguage());
        submissionDocument.setUserId(userDocument.getId());
        submissionDocument.setName(userDocument.getName());
        submissionDocument.setProblemId(submission.getProblem().getId());
        submissionDocument.setTitle(submission.getProblem().getTitle());
        submissionDocument.setTotalTest(submission.getProblem().getTestcase().size());
        submissionDocument.setCreatedAt(LocalDateTime.now().toString());
        return submissionRepository.insert(submissionDocument);
    }

    public Optional<SubmissionDocument> findOne(String id) {
        return submissionRepository.findById(id);
    }
    public void update(SubmissionDocument submissionDocument) {
        submissionRepository.save(submissionDocument);
    }

    public List<SubmissionDocument> getAll() {
        List<SubmissionDocument> list = submissionRepository.findAll();
        Collections.reverse(list);
        return list;
    }

    public Optional<SubmissionDocument> execute(String id) {
        return submissionRepository.findById(id);
    }

    public List<SubmissionDocument> getSubmissionOfProblem(String problemId, String email) {
        return submissionRepository.findByProblemIdAndUserId(problemId, Constant.getId(email));
    }

    public List<SubmissionDocument> getSubmissionByProblem(String problemId) {
        return submissionRepository.findByProblemId(problemId);
    }
}
