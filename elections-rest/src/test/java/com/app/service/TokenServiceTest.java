package com.app.service;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.exceptions.MyException;
import com.app.model.dto.VoterDto;
import com.app.repository.TokenRepository;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.Education.*;
import static com.app.model.Gender.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TokenServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private TokenRepository tokenRepository;
        @MockBean
        private ToolsService toolsService;

        @Bean
        public TokenService tokenService() {
            return new TokenService(tokenRepository, toolsService);
        }
    }

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ToolsService toolsService;

    @Test
    void should_successfully_add_new_token() {
        // given
        when(toolsService.findConstituencyByIdWithErrorCheck(anyLong())).thenReturn(createConstituency());
        when(tokenRepository.save(any())).thenReturn(createToken());

        // when
        tokenService.addToken(createVoterDto());

        // then
        verify(toolsService, times(1)).findConstituencyByIdWithErrorCheck(anyLong());
        verify(tokenRepository, times(1)).save(any());
        verify(tokenRepository, times(1)).findAll();
    }

    @Test
    void should_throw_exception_during_adding_null_token() {
        // given

        // when + then
        assertThrows(MyException.class, () -> tokenService.addToken(null));
    }

    @Test
    void should_successfully_delete_used_token() {
        // given

        // when
        String actualMessage = tokenService.deleteUsedToken(1234);

        // then
        assertEquals("Successfully deleted token", actualMessage);
    }

    @Test
    void should_successfully_find_voter_by_token() {
        // given
        when(tokenRepository.findAll()).thenReturn(Collections.singletonList(createToken()));

        // when
        VoterDto voterDto = tokenService.findVoterByToken(1234);

        // then
        assertNotNull(voterDto);
        assertEquals(1L, voterDto.getId());
        assertEquals(25, voterDto.getAge());
        assertEquals(MALE, voterDto.getGender());
        assertEquals(HIGHER_EDUCATION, voterDto.getEducation());
    }

    @Test
    void should_throw_exception_during_finding_voter_by_null_token() {
        // given

        // when + then
        assertThrows(MyException.class, () -> tokenService.findVoterByToken(null));
    }
}