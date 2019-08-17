package com.app.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.app.exceptions.MyException;
import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.security.TokenManager;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.Gender.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;

@ExtendWith(SpringExtension.class)
class CandidateServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private RestTemplate restTemplate;
        @MockBean
        private TokenManager tokenManager;

        @Bean
        public CandidateService candidateService() {
            return new CandidateService(restTemplate, tokenManager);
        }
    }

    @Autowired
    private CandidateService candidateService;
    @Autowired
    private RestTemplate restTemplate;

    private static CandidateDto candidateDto = createCandidateDto();

    @Test
    void should_successfully_add_new_candidate() {
        // given
        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.ok(candidateDto));

        // when
        CandidateDto actualCandidate = candidateService.addCandidate(candidateDto);

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertTrue(actualCandidate.getIsValid());

        ConstituencyDto actualConstituency = actualCandidate.getConstituencyDto();
        assertEquals(1L, actualConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualConstituency.getName());
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
        when(restTemplate.exchange(anyString(), eq(PUT), any(), eq(CandidateDto.class)))
            .thenReturn(ResponseEntity.ok(candidateDto));

        // when
        CandidateDto actualCandidate = candidateService.updateCandidate(candidateDto);

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertTrue(actualCandidate.getIsValid());

        ConstituencyDto actualConstituency = actualCandidate.getConstituencyDto();
        assertEquals(1L, actualConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualConstituency.getName());
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
        when(restTemplate.exchange(anyString(), eq(DELETE), any(), eq(CandidateDto.class), anyMap()))
            .thenReturn(ResponseEntity.ok(candidateDto));

        // when
        CandidateDto actualCandidate = candidateService.deleteCandidate(1L);

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertTrue(actualCandidate.getIsValid());

        ConstituencyDto actualConstituency = actualCandidate.getConstituencyDto();
        assertEquals(1L, actualConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualConstituency.getName());
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
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(CandidateDto.class), anyMap())).thenReturn(ResponseEntity.ok(candidateDto));

        // when
        CandidateDto actualCandidate = candidateService.getOneCandidate(1L);

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertTrue(actualCandidate.getIsValid());

        ConstituencyDto actualConstituency = actualCandidate.getConstituencyDto();
        assertEquals(1L, actualConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualConstituency.getName());
    }

    @Test
    void should_throw_exception_during_getting_candidate_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> candidateService.getOneCandidate(null));
    }

    @Test
    void should_successfully_find_candidates() {
        // given
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(CandidateDto[].class)))
            .thenReturn(ResponseEntity.ok(new CandidateDto[]{candidateDto}));

        // when
        List<CandidateDto> actualCandidates = candidateService.getAllCandidates();

        // then
        assertNotNull(actualCandidates);
        assertEquals(1, actualCandidates.size());

        CandidateDto actualCandidate = actualCandidates.get(0);
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertTrue(actualCandidate.getIsValid());

        ConstituencyDto actualConstituency = actualCandidate.getConstituencyDto();
        assertEquals(1L, actualConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualConstituency.getName());
    }

    @Test
    void should_throw_exception_during_getting_null_candidates() {
        // given
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(CandidateDto[].class))).thenReturn(null);

        // when + then
        assertThrows(MyException.class, () -> candidateService.getAllCandidates());
    }
}