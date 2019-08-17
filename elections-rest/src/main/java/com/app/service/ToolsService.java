package com.app.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.app.exceptions.MyException;
import com.app.model.Candidate;
import com.app.model.Constituency;
import com.app.model.Voter;
import com.app.model.security.User;
import com.app.repository.CandidateRepository;
import com.app.repository.ConstituencyRepository;
import com.app.repository.UserRepository;
import com.app.repository.VoterRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ToolsService {

    private final CandidateRepository candidateRepository;
    private final ConstituencyRepository constituencyRepository;
    private final VoterRepository voterRepository;
    private final UserRepository userRepository;

    public Candidate findCandidateByIdWithErrorCheck(final Long candidateId) {
        return candidateRepository.findById(candidateId)
            .orElseThrow(() -> new MyException("Missing candidate with id = " + candidateId));
    }

    public Constituency findConstituencyByIdWithErrorCheck(final Long constituencyId) {
        return constituencyRepository.findById(constituencyId)
            .orElseThrow(() -> new MyException("Missing constituency with id = " + constituencyId));
    }

    public Voter findVoterByIdWithErrorCheck(final Long voterId) {
        return voterRepository.findById(voterId)
            .orElseThrow(() -> new MyException("Missing voter with id = " + voterId));
    }

    public User findUserByIdWithErrorCheck(final Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new MyException("Missing user with id = " + userId));
    }
}
