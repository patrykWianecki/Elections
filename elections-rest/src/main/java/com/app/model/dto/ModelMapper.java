package com.app.model.dto;

import com.app.model.Candidate;
import com.app.model.Constituency;
import com.app.model.Voter;
import com.app.model.dto.security.UserDto;
import com.app.model.security.User;

public interface ModelMapper {

    static VoterDto fromVoterToVoterDto(Voter voter) {
        return voter == null ? null : VoterDto.builder()
            .id(voter.getId())
            .age(voter.getAge())
            .gender(voter.getGender())
            .education(voter.getEducation())
            .constituencyDto(voter.getConstituency() == null ? null : fromConstituencyToConstituencyDto(voter.getConstituency()))
            .build();
    }

    static Voter fromVoterDtoToVoter(VoterDto voterDto) {
        return voterDto == null ? null : Voter.builder()
            .id(voterDto.getId())
            .age(voterDto.getAge())
            .gender(voterDto.getGender())
            .education(voterDto.getEducation())
            .constituency(voterDto.getConstituencyDto() == null ? null : fromConstituencyDtoToConstituency(voterDto.getConstituencyDto()))
            .build();
    }

    static CandidateDto fromCandidateToCandidateDto(Candidate candidate) {
        return candidate == null ? null : CandidateDto.builder()
            .id(candidate.getId())
            .name(candidate.getName())
            .surname(candidate.getSurname())
            .age(candidate.getAge())
            .gender(candidate.getGender())
            .photo(candidate.getPhoto())
            .votes(candidate.getVotes())
            .isValid(candidate.getIsValid())
            .constituencyDto(candidate.getConstituency() == null ? null : fromConstituencyToConstituencyDto(candidate.getConstituency()))
            .build();
    }

    static Candidate fromCandidateDtoToCandidate(CandidateDto candidateDto) {
        return candidateDto == null ? null : Candidate.builder()
            .id(candidateDto.getId())
            .name(candidateDto.getName())
            .surname(candidateDto.getSurname())
            .age(candidateDto.getAge())
            .gender(candidateDto.getGender())
            .photo(candidateDto.getPhoto())
            .votes(candidateDto.getVotes())
            .isValid(candidateDto.getIsValid())
            .constituency(
                candidateDto.getConstituencyDto() == null ? null : fromConstituencyDtoToConstituency(candidateDto.getConstituencyDto()))
            .build();
    }

    static ConstituencyDto fromConstituencyToConstituencyDto(Constituency constituency) {
        return constituency == null ? null : ConstituencyDto.builder()
            .id(constituency.getId())
            .name(constituency.getName())
            .build();
    }

    static Constituency fromConstituencyDtoToConstituency(ConstituencyDto constituencyDto) {
        return constituencyDto == null ? null : Constituency.builder()
            .id(constituencyDto.getId())
            .name(constituencyDto.getName())
            .build();
    }

    static UserDto fromUserToUserDto(User user) {
        return user == null ? null : UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .enabled(user.getEnabled())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
    }

    static User fromUserDtoToUser(UserDto userDto) {
        return userDto == null ? null : User.builder()
            .id(userDto.getId())
            .username(userDto.getUsername())
            .password(userDto.getPassword())
            .enabled(userDto.getEnabled())
            .email(userDto.getEmail())
            .role(userDto.getRole())
            .build();
    }
}
