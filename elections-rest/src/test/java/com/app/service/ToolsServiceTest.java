package com.app.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.exceptions.MyException;
import com.app.model.Candidate;
import com.app.model.Constituency;
import com.app.model.Voter;
import com.app.model.security.User;
import com.app.repository.CandidateRepository;
import com.app.repository.ConstituencyRepository;
import com.app.repository.UserRepository;
import com.app.repository.VoterRepository;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.Gender.*;
import static com.app.model.security.Role.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ToolsServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private CandidateRepository candidateRepository;
        @MockBean
        private ConstituencyRepository constituencyRepository;
        @MockBean
        private VoterRepository voterRepository;
        @MockBean
        private UserRepository userRepository;

        @Bean
        public ToolsService toolsService() {
            return new ToolsService(candidateRepository, constituencyRepository, voterRepository, userRepository);
        }
    }

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private ConstituencyRepository constituencyRepository;
    @Autowired
    private VoterRepository voterRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ToolsService toolsService;

    @Test
    void should_successfully_find_candidate_by_id() {
        // given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(createCandidate()));

        // when
        Candidate actualCandidate = toolsService.findCandidateByIdWithErrorCheck(1L);

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertEquals(true, actualCandidate.getIsValid());
    }

    @Test
    void should_throw_exception_during_finding_candidate_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> toolsService.findCandidateByIdWithErrorCheck(null));
    }

    @Test
    void should_successfully_find_constituency_by_id() {
        // given
        when(constituencyRepository.findById(anyLong())).thenReturn(Optional.of(createConstituency()));

        // when
        Constituency actualConstituency = toolsService.findConstituencyByIdWithErrorCheck(1L);

        // then
        assertNotNull(actualConstituency);
        assertEquals(1L, actualConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY", actualConstituency.getName());
    }

    @Test
    void should_throw_exception_during_finding_constituency_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> toolsService.findConstituencyByIdWithErrorCheck(null));
    }

    @Test
    void should_successfully_find_voter_by_id() {
        // given
        when(voterRepository.findById(anyLong())).thenReturn(Optional.of(createVoter()));

        // when
        Voter actualVoter = toolsService.findVoterByIdWithErrorCheck(1L);

        // then
        assertNotNull(actualVoter);
        assertEquals(1L, actualVoter.getId());
        assertEquals(25, actualVoter.getAge());
        assertEquals(MALE, actualVoter.getGender());
    }

    @Test
    void should_throw_exception_during_finding_voter_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> toolsService.findVoterByIdWithErrorCheck(null));
    }

    @Test
    void should_successfully_find_user_by_id() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));

        // when
        User actualUser = toolsService.findUserByIdWithErrorCheck(1L);

        // then
        assertNotNull(actualUser);
        assertEquals(1L, actualUser.getId());
        assertEquals("username", actualUser.getUsername());
        assertEquals("password", actualUser.getPassword());
        assertEquals(ADMIN, actualUser.getRole());
        assertEquals("user@gmail.com", actualUser.getEmail());
        assertEquals(true, actualUser.getEnabled());
    }

    @Test
    void should_throw_exception_during_finding_user_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> toolsService.findUserByIdWithErrorCheck(null));
    }
}