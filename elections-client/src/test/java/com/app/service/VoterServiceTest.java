package com.app.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.app.exceptions.MyException;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.VoterDto;
import com.app.security.TokenManager;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.Education.*;
import static com.app.model.Gender.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;

@ExtendWith(SpringExtension.class)
class VoterServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private RestTemplate restTemplate;
        @MockBean
        private TokenManager tokenManager;

        @Bean
        public VoterService voterService() {
            return new VoterService(restTemplate, tokenManager);
        }
    }

    @Autowired
    private VoterService voterService;
    @Autowired
    private RestTemplate restTemplate;

    private static VoterDto voterDto = createVoterDto();

    @Test
    void should_successfully_add_voter() {
        // given
        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.ok(voterDto));

        // when
        VoterDto actualVoterDto = voterService.addVoter(createVoterDto());

        // then
        assertNotNull(actualVoterDto);
        assertEquals(1L, actualVoterDto.getId());
        assertEquals(25, actualVoterDto.getAge());
        assertEquals(MALE, actualVoterDto.getGender());
        assertEquals(HIGHER_EDUCATION, actualVoterDto.getEducation());

        ConstituencyDto actualVoterConstituency = actualVoterDto.getConstituencyDto();
        assertEquals(1L, actualVoterConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualVoterConstituency.getName());
    }

    @Test
    void should_throw_exception_during_adding_null_voter() {
        // given

        // when + then
        assertThrows(MyException.class, () -> voterService.addVoter(null));
    }

    @Test
    void should_successfully_update_voter() {
        // given
        when(restTemplate.exchange(anyString(), eq(PUT), any(), eq(VoterDto.class))).thenReturn(ResponseEntity.ok(voterDto));

        // when
        VoterDto actualVoterDto = voterService.updateVoter(createVoterDto());

        // then
        assertNotNull(actualVoterDto);
        assertEquals(1L, actualVoterDto.getId());
        assertEquals(25, actualVoterDto.getAge());
        assertEquals(MALE, actualVoterDto.getGender());
        assertEquals(HIGHER_EDUCATION, actualVoterDto.getEducation());

        ConstituencyDto actualVoterConstituency = actualVoterDto.getConstituencyDto();
        assertEquals(1L, actualVoterConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualVoterConstituency.getName());
    }

    @Test
    void should_throw_exception_during_updating_null_voter() {
        // given

        // when + then
        assertThrows(MyException.class, () -> voterService.updateVoter(null));
    }

    @Test
    void should_successfully_delete_voter() {
        // given
        when(restTemplate.exchange(anyString(), eq(DELETE), any(), eq(VoterDto.class), anyMap())).thenReturn(ResponseEntity.ok(voterDto));

        // when
        VoterDto actualVoterDto = voterService.deleteVoter(1L);

        // then
        assertNotNull(actualVoterDto);
        assertEquals(1L, actualVoterDto.getId());
        assertEquals(25, actualVoterDto.getAge());
        assertEquals(MALE, actualVoterDto.getGender());
        assertEquals(HIGHER_EDUCATION, actualVoterDto.getEducation());

        ConstituencyDto actualVoterConstituency = actualVoterDto.getConstituencyDto();
        assertEquals(1L, actualVoterConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualVoterConstituency.getName());
    }

    @Test
    void should_throw_exception_during_deleting_voter_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> voterService.deleteVoter(null));
    }

    @Test
    void should_successfully_find_one_voter() {
        // given
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(VoterDto.class), anyMap())).thenReturn(ResponseEntity.ok(voterDto));

        // when
        VoterDto actualVoterDto = voterService.getOneVoter(1L);

        // then
        assertNotNull(actualVoterDto);
        assertEquals(1L, actualVoterDto.getId());
        assertEquals(25, actualVoterDto.getAge());
        assertEquals(MALE, actualVoterDto.getGender());
        assertEquals(HIGHER_EDUCATION, actualVoterDto.getEducation());

        ConstituencyDto actualVoterConstituency = actualVoterDto.getConstituencyDto();
        assertEquals(1L, actualVoterConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualVoterConstituency.getName());
    }

    @Test
    void should_throw_exception_during_getting_voter_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> voterService.getOneVoter(null));
    }

    @Test
    void should_successfully_find_all_voters() {
        // given
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(VoterDto[].class)))
            .thenReturn(ResponseEntity.ok(new VoterDto[]{voterDto}));

        // when
        List<VoterDto> actualVotersDto = voterService.getAllVoters();

        // then
        assertNotNull(actualVotersDto);
        assertEquals(1, actualVotersDto.size());

        VoterDto actualVoterDto = actualVotersDto.get(0);
        assertEquals(1L, actualVoterDto.getId());
        assertEquals(25, actualVoterDto.getAge());
        assertEquals(MALE, actualVoterDto.getGender());
        assertEquals(HIGHER_EDUCATION, actualVoterDto.getEducation());

        ConstituencyDto actualVoterConstituency = actualVoterDto.getConstituencyDto();
        assertEquals(1L, actualVoterConstituency.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualVoterConstituency.getName());
    }

    @Test
    void should_throw_exception_during_getting_null_voters() {
        // given
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(VoterDto[].class))).thenReturn(null);

        // when + then
        assertThrows(MyException.class, () -> voterService.getAllVoters());
    }
}