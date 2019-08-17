package com.app.builders;

import com.app.model.Candidate;
import com.app.model.Constituency;
import com.app.model.Token;
import com.app.model.Voter;
import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.TokenDto;
import com.app.model.dto.VoterDto;
import com.app.model.dto.security.UserDto;
import com.app.model.security.User;

import static com.app.model.Education.*;
import static com.app.model.Gender.*;
import static com.app.model.security.Role.*;

public interface MockDataForTests {

    static Candidate createCandidate() {
        return Candidate.builder()
            .id(1L)
            .name("Name")
            .surname("Surname")
            .age(20)
            .gender(MALE)
            .isValid(true)
            .votes(10)
            .constituency(createConstituency())
            .photo(null)
            .build();
    }

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
            .multipartFile(null)
            .build();
    }

    static Constituency createConstituency() {
        return Constituency.builder()
            .id(1L)
            .name("FIRST_CONSTITUENCY")
            .build();
    }

    static ConstituencyDto createConstituencyDto() {
        return ConstituencyDto.builder()
            .id(1L)
            .name("FIRST_CONSTITUENCY_DTO")
            .build();
    }

    static Voter createVoter() {
        return Voter.builder()
            .id(1L)
            .age(25)
            .gender(MALE)
            .education(HIGHER_EDUCATION)
            .constituency(createConstituency())
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

    static Token createToken() {
        return Token.builder()
            .id(1L)
            .voterToken(1234)
            .voter(createVoter())
            .build();
    }

    static TokenDto createTokenDto() {
        return TokenDto.builder()
            .id(1L)
            .voterToken(1234)
            .build();
    }

    static User createUser() {
        return User.builder()
            .id(1L)
            .username("username")
            .password("password")
            .role(ADMIN)
            .email("user@gmail.com")
            .enabled(true)
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
}
