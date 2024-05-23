package com.example.oj.service;

import com.example.oj.document.DetailContest;
import com.example.oj.document.UserDocument;
import com.example.oj.dto.Submission;
import com.example.oj.dto.TaskContest;
import com.example.oj.repository.DetailContestRepository;
import com.example.oj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetailContestService {
    private final DetailContestRepository detailContestRepository;
    private final UserRepository userRepository;

    public DetailContest insert(Submission submission, String email) {
        UserDocument userDocument = userRepository.findOneByEmail(email);
        DetailContest detailContest = new DetailContest();
        detailContest.setSource(submission.getCode());
        detailContest.setLanguage(submission.getLanguage());
        detailContest.setUserId(userDocument.getId());
        detailContest.setName(userDocument.getName());
        detailContest.setProblemId(submission.getProblem().getId());
        detailContest.setTitle(submission.getProblem().getTitle());
        detailContest.setTotalTest(submission.getProblem().getTestcase().size());
        detailContest.setCreatedAt(LocalDateTime.now().toString());
        detailContest.setNameContest(submission.getContest().getTitle());
        detailContest.setContestId(submission.getContest().getId());
        for (TaskContest task: submission.getContest().getProblems()) {
            if(task.getId().equals(submission.getProblem().getId())) {
                detailContest.setMaxScored((float) task.getPoint());
                break;
            }
        }
        return detailContestRepository.insert(detailContest);
    }

    public void update(DetailContest detailContest) {
        detailContestRepository.save(detailContest);
    }

    public Optional<DetailContest> findById(String id) {
        return detailContestRepository.findById(id);
    }

    public List<DetailContest> getAll(String contestId) {
        return detailContestRepository.findByContestId(contestId);
    }

    public List<DetailContest> getByContestIdAndUserId(String contestId, String userId) {
        return detailContestRepository.findByContestIdAndUserId(contestId, userId);
    }
    public List<DetailContest> getByContestIdAndProblemId(String contestId, String problemId) {
        return detailContestRepository.findByContestIdAndProblemId(contestId, problemId);
    }
}
