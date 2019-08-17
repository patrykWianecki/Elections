package com.app.builders;

import java.util.List;

import com.app.model.Constituency;
import com.app.model.Voter;
import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.TokenDto;
import com.app.model.dto.VoterDto;
import com.app.model.dto.VotingResult;

import static com.app.model.Education.*;
import static com.app.model.Gender.*;

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

    static ConstituencyDto createSecondConstituencyDto() {
        return ConstituencyDto.builder()
            .id(2L)
            .name("SECOND_CONSTITUENCY_DTO")
            .build();
    }

    static ConstituencyDto createThirdConstituencyDto() {
        return ConstituencyDto.builder()
            .id(3L)
            .name("THIRD_CONSTITUENCY_DTO")
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

    static TokenDto createTokenDto() {
        return TokenDto.builder()
            .id(1L)
            .voterToken(1234)
            .build();
    }

    static List<CandidateDto> createCandidates() {
        return List.of(
            CandidateDto.builder()
                .id(1L)
                .name("Name1")
                .surname("Surname1")
                .age(20)
                .gender(MALE)
                .isValid(true)
                .votes(10)
                .constituencyDto(createConstituencyDto())
                .build(),
            CandidateDto.builder()
                .id(2L)
                .name("Name2")
                .surname("Surname2")
                .age(21)
                .gender(FEMALE)
                .isValid(true)
                .votes(11)
                .constituencyDto(createConstituencyDto())
                .build(),
            CandidateDto.builder()
                .id(3L)
                .name("Name3")
                .surname("Surname3")
                .age(23)
                .gender(MALE)
                .isValid(false)
                .votes(12)
                .constituencyDto(createConstituencyDto())
                .build(),
            CandidateDto.builder()
                .id(4L)
                .name("Name4")
                .surname("Surname4")
                .age(34)
                .gender(MALE)
                .isValid(true)
                .votes(1)
                .constituencyDto(createSecondConstituencyDto())
                .build(),
            CandidateDto.builder()
                .id(5L)
                .name("Name5")
                .surname("Surname5")
                .age(43)
                .gender(FEMALE)
                .isValid(true)
                .votes(99)
                .constituencyDto(createSecondConstituencyDto())
                .build(),
            CandidateDto.builder()
                .id(6L)
                .name("Name6")
                .surname("Surname6")
                .age(50)
                .gender(FEMALE)
                .isValid(true)
                .votes(20)
                .constituencyDto(createThirdConstituencyDto())
                .build(),
            CandidateDto.builder()
                .id(7L)
                .name("Name7")
                .surname("Surname7")
                .age(65)
                .gender(MALE)
                .isValid(true)
                .votes(55)
                .constituencyDto(createThirdConstituencyDto())
                .build()
        );
    }

    static List<ConstituencyDto> createConstituencies() {
        return List.of(
            createConstituencyDto(),
            createSecondConstituencyDto(),
            createThirdConstituencyDto()
        );
    }

    static List<VoterDto> createVoters() {
        return List.of(
            VoterDto.builder()
                .id(1L)
                .age(30)
                .gender(MALE)
                .build(),
            VoterDto.builder()
                .id(2L)
                .age(40)
                .gender(MALE)
                .build(),
            VoterDto.builder()
                .id(3L)
                .age(50)
                .gender(FEMALE)
                .build()
        );
    }
}
