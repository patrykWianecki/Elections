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
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.VoterDto;
import com.app.repository.TokenRepository;
import com.app.repository.VoterRepository;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.Education.*;
import static com.app.model.Gender.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class VoterServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private VoterRepository voterRepository;
        @MockBean
        private TokenRepository tokenRepository;
        @MockBean
        private TokenService tokenService;
        @MockBean
        private ToolsService toolsService;

        @Bean
        public VoterService voterService() {
            return new VoterService(voterRepository, tokenRepository, tokenService, toolsService);
        }
    }

    @Autowired
    private VoterRepository voterRepository;
    @Autowired
    private VoterService voterService;
    @Autowired
    private ToolsService toolsService;

    @Test
    void should_successfully_add_voter() {
        // given
        when(voterRepository.save(any())).thenReturn(createVoter());

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
        when(voterRepository.save(any())).thenReturn(createVoter());
        when(toolsService.findConstituencyByIdWithErrorCheck(anyLong())).thenReturn(createConstituency());
        when(toolsService.findVoterByIdWithErrorCheck(anyLong())).thenReturn(createVoter());

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
        assertEquals("FIRST_CONSTITUENCY", actualVoterConstituency.getName());
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
        when(toolsService.findVoterByIdWithErrorCheck(anyLong())).thenReturn(createVoter());

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
        assertEquals("FIRST_CONSTITUENCY", actualVoterConstituency.getName());
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
        when(toolsService.findVoterByIdWithErrorCheck(anyLong())).thenReturn(createVoter());

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
        assertEquals("FIRST_CONSTITUENCY", actualVoterConstituency.getName());
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
        when(voterRepository.findAll()).thenReturn(Collections.singletonList(createVoter()));

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
        assertEquals("FIRST_CONSTITUENCY", actualVoterConstituency.getName());
    }

    @Test
    void should_throw_exception_during_getting_null_voters() {
        // given
        when(voterRepository.findAll()).thenReturn(null);

        // when + then
        assertThrows(MyException.class, () -> voterService.getAllVoters());
    }

    @Test
    void should_successfully_delete_all_voters_with_tokens() {
        // given

        // when
        String actualMessage = voterService.deleteAllVotersWithTokens();

        // then
        assertEquals("Deleted all voters with tokens", actualMessage);
    }
}