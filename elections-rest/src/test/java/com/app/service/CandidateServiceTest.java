package com.app.service;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.exceptions.MyException;
import com.app.model.dto.CandidateDto;
import com.app.repository.CandidateRepository;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.Gender.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CandidateServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private CandidateRepository candidateRepository;
        @MockBean
        private ToolsService toolsService;

        @Bean
        public CandidateService candidateService() {
            return new CandidateService(candidateRepository, toolsService);
        }
    }

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private ToolsService toolsService;

    @Test
    void should_successfully_add_candidate() {
        // given
        when(candidateRepository.save(any())).thenReturn(createCandidate());

        // when
        CandidateDto actualCandidate = candidateService.addCandidate(createCandidateDto());

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertTrue(actualCandidate.getIsValid());
        assertEquals("FIRST_CONSTITUENCY", actualCandidate.getConstituencyDto().getName());
        assertEquals(1L, actualCandidate.getConstituencyDto().getId());
    }

    @Test
    void should_throw_exception_during_adding_null_candidate() {
        // given

        // when + then
        assertThrows(MyException.class, () -> candidateService.addCandidate(null));
    }

    @Test
    void should_successfully_update_candidate() {
        // given
        when(toolsService.findCandidateByIdWithErrorCheck(anyLong())).thenReturn(createCandidate());
        when(toolsService.findConstituencyByIdWithErrorCheck(anyLong())).thenReturn(createConstituency());
        when(candidateRepository.save(any())).thenReturn(createCandidate());

        // when
        CandidateDto actualCandidate = candidateService.updateCandidate(createCandidateDto());

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertEquals("FIRST_CONSTITUENCY", actualCandidate.getConstituencyDto().getName());
        assertEquals(1L, actualCandidate.getConstituencyDto().getId());
    }

    @Test
    void should_throw_exception_during_updating_null_candidate() {
        // given

        // when + then
        assertThrows(MyException.class, () -> candidateService.updateCandidate(null));
    }

    @Test
    void should_successfully_delete_candidate() {
        // given
        when(toolsService.findCandidateByIdWithErrorCheck(anyLong())).thenReturn(createCandidate());

        // when
        CandidateDto actualCandidate = candidateService.deleteCandidate(1L);

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertEquals("FIRST_CONSTITUENCY", actualCandidate.getConstituencyDto().getName());
        assertEquals(1L, actualCandidate.getConstituencyDto().getId());
    }

    @Test
    void should_throw_exception_during_deleting_candidate_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> candidateService.deleteCandidate(null));
    }

    @Test
    void should_successfully_find_one_candidate() {
        // given
        when(toolsService.findCandidateByIdWithErrorCheck(anyLong())).thenReturn(createCandidate());

        // when
        CandidateDto actualCandidate = candidateService.getOneCandidate(1L);

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertEquals("FIRST_CONSTITUENCY", actualCandidate.getConstituencyDto().getName());
        assertEquals(1L, actualCandidate.getConstituencyDto().getId());
    }

    @Test
    void should_throw_exception_during_getting_candidate_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> candidateService.getOneCandidate(null));
    }

    @Test
    void should_successfully_find_all_candidates() {
        // given
        when(candidateRepository.findAll()).thenReturn(Collections.singletonList(createCandidate()));

        // when
        List<CandidateDto> actualCandidates = candidateService.getAllCandidates();

        // then
        assertNotNull(actualCandidates);
        assertEquals(1, actualCandidates.size());

        CandidateDto actualCandidate = actualCandidates.get(0);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertEquals("FIRST_CONSTITUENCY", actualCandidate.getConstituencyDto().getName());
        assertEquals(1L, actualCandidate.getConstituencyDto().getId());
    }

    @Test
    void should_throw_exception_during_getting_null_candidates() {
        // given
        when(candidateRepository.findAll()).thenReturn(null);

        // when + then
        assertThrows(MyException.class, () -> candidateService.getAllCandidates());
    }
}