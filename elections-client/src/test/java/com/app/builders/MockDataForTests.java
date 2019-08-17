package com.app.builders;

import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.TokenDto;
import com.app.model.dto.VoterDto;
import com.app.model.dto.security.UserDto;

import static com.app.model.Education.*;
import static com.app.model.Gender.*;
import static com.app.model.security.Role.*;

public interface MockDataForTests {

    static CandidateDto createCandidateDto() {
        return CandidateDto.builder()
            .id(1L)
            .name("Name")
            .surname("Surname")
            .age(20)
            .gender(MALE)
            .isValid(true)
            .votes(10)
            .constituencyDto(createConstituencyDto())
            .photo(null)
            .build();
    }

    static ConstituencyDto createConstituencyDto() {
        return ConstituencyDto.builder()
            .id(1L)
            .name("FIRST_CONSTITUENCY_DTO")
            .build();
    }

    static VoterDto createVoterDto() {
        return VoterDto.builder()
            .id(1L)
            .age(25)
            .gender(MALE)
            .education(HIGHER_EDUCATION)
            .constituencyDto(createConstituencyDto())
            .tokenDto(createTokenDto())
            .build();
    }

    static TokenDto createTokenDto() {
        return TokenDto.builder()
            .id(1L)
            .voterToken(1234)
            .build();
    }

    static UserDto createUserDto() {
        return UserDto.builder()
            .id(1L)
            .username("username")
            .password("password")
            .role(ADMIN)
            .email("user@gmail.com")
            .enabled(true)
            .build();
    }

    static CandidateDto createValidCandidate() {
        return CandidateDto.builder()
            .id(1L)
            .name("NAME NAME")
            .surname("SURNAME SURNAME")
            .age(20)
            .gender(MALE)
            .isValid(true)
            .votes(10)
            .constituencyDto(createConstituencyDto())
            .photo(null)
            .build();
    }

    static CandidateDto createInvalidCandidate() {
        return CandidateDto.builder()
            .id(1L)
            .name("Name")
            .surname("Surname")
            .age(15)
            .build();
    }

    static ConstituencyDto createValidConstituency() {
        return ConstituencyDto.builder()
            .id(1L)
            .name("FIRST CONSTITUENCY")
            .build();
    }

    static ConstituencyDto createInvalidConstituency() {
        return ConstituencyDto.builder()
            .id(1L)
            .name("First constituency")
            .build();
    }

    static UserDto createValidUser() {
        return UserDto.builder()
            .id(1L)
            .username("username")
            .password("password")
            .role(ADMIN)
            .email("user@gmail.com")
            .enabled(true)
            .build();
    }

    static UserDto createInvalidUser() {
        return UserDto.builder()
            .id(1L)
            .username("USERNAME")
            .password("PASSWORD")
            .email("mail.com")
            .build();
    }

    static VoterDto createValidVoter() {
        return VoterDto.builder()
            .id(1L)
            .age(25)
            .gender(MALE)
            .education(HIGHER_EDUCATION)
            .constituencyDto(createConstituencyDto())
            .tokenDto(createTokenDto())
            .build();
    }

    static VoterDto createInvalidVoter() {
        return VoterDto.builder()
            .id(1L)
            .age(17)
            .tokenDto(createTokenDto())
            .build();
    }
}
