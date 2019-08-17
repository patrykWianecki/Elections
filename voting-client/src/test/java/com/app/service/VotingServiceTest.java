package com.app.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import com.app.model.Candidate;
import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.TokenDto;
import com.app.model.dto.VoterDto;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.Gender.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;

@ExtendWith(SpringExtension.class)
class VotingServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private RestTemplate restTemplate;

        @Bean
        public VotingService votingService() {
            return new VotingService(restTemplate);
        }
    }

    @Autowired
    private VotingService votingService;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    void should_successfully_update_candidate() {
        // given
        CandidateDto candidateDto = createCandidateDto();
        when(restTemplate.exchange(anyString(), eq(PUT), any(), eq(CandidateDto.class)))
            .thenReturn(ResponseEntity.ok(candidateDto));

        // when
        CandidateDto actualCandidate = votingService.updateCandidate(candidateDto);

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertEquals(10, actualCandidate.getVotes());
        assertTrue(actualCandidate.getIsValid());
    }

    @Test
    void should_throw_exception_during_updating_candidate() {
        // given

        // when + then
        assertThrows(MyException.class, () -> votingService.updateCandidate(null));
    }

    @Test
    void should_successfully_find_candidate_by_id() {
        // given
        when(restTemplate.getForEntity(anyString(), eq(CandidateDto.class), anyMap())).thenReturn(ResponseEntity.ok(createCandidateDto()));

        // when
        CandidateDto actualCandidate = votingService.findCandidateById(1L);

        // then
        assertNotNull(actualCandidate);
        assertEquals(1L, actualCandidate.getId());
        assertEquals("Name", actualCandidate.getName());
        assertEquals("Surname", actualCandidate.getSurname());
        assertEquals(20, actualCandidate.getAge());
        assertEquals(MALE, actualCandidate.getGender());
        assertEquals(10, actualCandidate.getVotes());
        assertTrue(actualCandidate.getIsValid());
    }

    @Test
    void should_throw_exception_during_finding_candidate_without_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> votingService.findCandidateById(null));
    }

    @Test
    void should_successfully_remove_used_token() {
        // given
        when(restTemplate.exchange(anyString(), eq(DELETE), any(), eq(TokenDto.class), anyMap()))
            .thenReturn(ResponseEntity.ok(createTokenDto
                ()));

        // when
        TokenDto tokenDto = votingService.removeUsedToken(1234);

        // then
        assertNotNull(tokenDto);
    }

    @Test
    void should_throw_exception_during_removing_token() {
        // given

        // when + then
        assertThrows(MyException.class, () -> votingService.removeUsedToken(null));
    }

    @Test
    void should_successfully_find_voter_by_token() {
        // given
        when(restTemplate.getForEntity(anyString(), eq(VoterDto.class), anyMap())).thenReturn(ResponseEntity.ok(createVoterDto()));

        // when
        VoterDto voterDto = votingService.findVoterByToken("1234");

        // then
        assertNotNull(voterDto);
    }

    @Test
    void should_throw_exception_during_finding_voter_without_token() {
        // given

        // when + then
        assertThrows(MyException.class, () -> votingService.findVoterByToken(null));
    }

    @Test
    void should_successfully_find_all_available_candidates_for_voter() {
        // given
        when(restTemplate.getForEntity(anyString(), eq(VoterDto.class), anyMap())).thenReturn(ResponseEntity.ok(createVoterDto()));
        when(restTemplate.getForObject(anyString(), eq(CandidateDto[].class))).thenReturn(createCandidates().toArray(new CandidateDto[4]));

        // when
        List<CandidateDto> actualCandidates = votingService.findAllCandidatesForVoterWithGivenToken("1234");

        // then
        assertNotNull(actualCandidates);
        assertEquals(2, actualCandidates.size());

        CandidateDto candidate1 = actualCandidates.get(0);
        assertNotNull(candidate1);
        assertEquals(1L, candidate1.getId());
        assertEquals("Name1", candidate1.getName());
        assertEquals("Surname1", candidate1.getSurname());
        assertEquals(20, candidate1.getAge());
        assertEquals(MALE, candidate1.getGender());
        assertEquals(10, candidate1.getVotes());
        assertTrue(candidate1.getIsValid());

        ConstituencyDto constituency1 = candidate1.getConstituencyDto();
        assertNotNull(constituency1);
        assertEquals(1, constituency1.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", constituency1.getName());

        CandidateDto candidate2 = actualCandidates.get(1);
        assertNotNull(candidate2);
        assertEquals(2L, candidate2.getId());
        assertEquals("Name2", candidate2.getName());
        assertEquals("Surname2", candidate2.getSurname());
        assertEquals(21, candidate2.getAge());
        assertEquals(FEMALE, candidate2.getGender());
        assertEquals(11, candidate2.getVotes());
        assertTrue(candidate1.getIsValid());

        ConstituencyDto constituency2 = candidate2.getConstituencyDto();
        assertNotNull(constituency2);
        assertEquals(1, constituency2.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", constituency2.getName());
    }

    @Test
    void should_throw_exception_during_finding_all_available_candidates_for_voter_with_missing_token() {
        // given

        // when + then
        assertThrows(MyException.class, () -> votingService.findAllCandidatesForVoterWithGivenToken(null));
    }

    @Test
    void should_throw_exception_during_finding_all_available_candidates_for_voter_with_missing_constituency() {
        // given
        when(restTemplate.getForEntity(anyString(), eq(VoterDto.class), anyMap())).thenReturn(ResponseEntity.ok(createVoterDto()));
        when(restTemplate.getForObject(anyString(), eq(CandidateDto[].class))).thenThrow(MyException.class);

        // when + then
        assertThrows(MyException.class, () -> votingService.findAllCandidatesForVoterWithGivenToken("1234"));
    }

    @Test
    void should_successfully_find_winners_from_each_constituency() {
        // given
        when(restTemplate.getForObject(anyString(), eq(CandidateDto[].class))).thenReturn(createCandidates().toArray(new CandidateDto[6]));

        // when
        List<CandidateDto> actualCandidates = votingService.findWinnersInEachConstituency();

        // then
        assertNotNull(actualCandidates);
        assertEquals(3, actualCandidates.size());

        CandidateDto candidate1 = actualCandidates.get(0);
        assertNotNull(candidate1);
        assertEquals(5L, candidate1.getId());
        assertEquals("Name5", candidate1.getName());
        assertEquals("Surname5", candidate1.getSurname());
        assertEquals(43, candidate1.getAge());
        assertEquals(FEMALE, candidate1.getGender());
        assertEquals(99, candidate1.getVotes());
        assertTrue(candidate1.getIsValid());

        ConstituencyDto constituency1 = candidate1.getConstituencyDto();
        assertNotNull(constituency1);
        assertEquals(2, constituency1.getId());
        assertEquals("SECOND_CONSTITUENCY_DTO", constituency1.getName());

        CandidateDto candidate2 = actualCandidates.get(1);
        assertNotNull(candidate2);
        assertEquals(7L, candidate2.getId());
        assertEquals("Name7", candidate2.getName());
        assertEquals("Surname7", candidate2.getSurname());
        assertEquals(65, candidate2.getAge());
        assertEquals(MALE, candidate2.getGender());
        assertEquals(55, candidate2.getVotes());
        assertTrue(candidate1.getIsValid());

        ConstituencyDto constituency2 = candidate2.getConstituencyDto();
        assertNotNull(constituency2);
        assertEquals(3, constituency2.getId());
        assertEquals("THIRD_CONSTITUENCY_DTO", constituency2.getName());

        CandidateDto candidate3 = actualCandidates.get(2);
        assertNotNull(candidate3);
        assertEquals(3L, candidate3.getId());
        assertEquals("Name3", candidate3.getName());
        assertEquals("Surname3", candidate3.getSurname());
        assertEquals(23, candidate3.getAge());
        assertEquals(MALE, candidate3.getGender());
        assertEquals(12, candidate3.getVotes());
        assertTrue(candidate1.getIsValid());

        ConstituencyDto constituency3 = candidate3.getConstituencyDto();
        assertNotNull(constituency3);
        assertEquals(1, constituency3.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", constituency3.getName());
    }

    @Test
    void should_throw_exception_during_finding_winners_from_each_constituency() {
        // given
        when(restTemplate.getForObject(anyString(), eq(CandidateDto[].class))).thenThrow(MyException.class);

        // when + then
        assertThrows(MyException.class, () -> votingService.findWinnersInEachConstituency());
    }

    @Test
    void should_successfully_count_average_voters_age() {
        // given
        when(restTemplate.getForObject(anyString(), eq(VoterDto[].class))).thenReturn(createVoters().toArray(new VoterDto[2]));

        // when
        double avgAge = votingService.countAverageVotersAge();

        // then
        assertEquals(40.0, avgAge);
    }

    @Test
    void should_throw_exception_during_counting_average_voters_age() {
        // given
        when(restTemplate.getForObject(anyString(), eq(VoterDto[].class))).thenThrow(MyException.class);

        // when + then
        assertThrows(MyException.class, () -> votingService.countAverageVotersAge());
    }

    @Test
    void should_successfully_count_genders_percentage() {
        // given
        when(restTemplate.getForObject(anyString(), eq(VoterDto[].class))).thenReturn(createVoters().toArray(new VoterDto[2]));

        // when
        Map<String, BigDecimal> actualGendersWithPercentages = votingService.countGenderPercentage();

        // then
        assertNotNull(actualGendersWithPercentages);
        assertEquals(2, actualGendersWithPercentages.size());
        assertEquals(BigDecimal.valueOf(66.67), actualGendersWithPercentages.get(MALE.name()));
        assertEquals(BigDecimal.valueOf(33.33), actualGendersWithPercentages.get(FEMALE.name()));
    }

    @Test
    void should_throw_exception_during_counting_genders_percentage() {
        // given
        when(restTemplate.getForObject(anyString(), eq(VoterDto[].class))).thenReturn(null);

        // when + then
        assertThrows(MyException.class, () -> votingService.countGenderPercentage());
    }

    @Test
    void should_successfully_group_candidates_with_their_percentage_result_in_constituencies() {
        // given
        when(restTemplate.getForObject(anyString(), eq(CandidateDto[].class))).thenReturn(createCandidates().toArray(new CandidateDto[6]));
        when(restTemplate.getForObject(anyString(), eq(ConstituencyDto[].class)))
            .thenReturn(createConstituencies().toArray(new ConstituencyDto[2]));

        // when
        Map<ConstituencyDto, Map<CandidateDto, BigDecimal>> candidatesWithPercentageResultsInConstituencies =
            votingService.groupCandidatesWithPercentageResultsInConstituencies();

        // then
        assertNotNull(candidatesWithPercentageResultsInConstituencies);
        assertEquals(3, candidatesWithPercentageResultsInConstituencies.size());

        ConstituencyDto firstConstituency = candidatesWithPercentageResultsInConstituencies.keySet().stream()
            .filter(constituencyDto -> "FIRST_CONSTITUENCY_DTO".equals(constituencyDto.getName())).findFirst().orElse(null);
        assertEquals("FIRST_CONSTITUENCY_DTO", firstConstituency.getName());

        ConstituencyDto secondConstituency = candidatesWithPercentageResultsInConstituencies.keySet().stream()
            .filter(constituencyDto -> "SECOND_CONSTITUENCY_DTO".equals(constituencyDto.getName())).findFirst().orElse(null);
        assertEquals("SECOND_CONSTITUENCY_DTO", secondConstituency.getName());

        ConstituencyDto thirdConstituency = candidatesWithPercentageResultsInConstituencies.keySet().stream()
            .filter(constituencyDto -> "THIRD_CONSTITUENCY_DTO".equals(constituencyDto.getName())).findFirst().orElse(null);
        assertEquals("THIRD_CONSTITUENCY_DTO", thirdConstituency.getName());
    }

    @Test
    void should_throw_exception_during_grouping_candidates_with_their_percentage_result_in_constituencies() {
        // given
        when(restTemplate.getForObject(anyString(), eq(ConstituencyDto[].class))).thenReturn(null);

        // when + then
        assertThrows(MyException.class, () -> votingService.groupCandidatesWithPercentageResultsInConstituencies());
    }

    @Test
    void should_successfully_go_to_second_ballot() {
        // given
        when(restTemplate.exchange(anyString(), eq(PUT), any(), eq(CandidateDto.class)))
            .thenReturn(ResponseEntity.ok(createCandidateDto()));
        when(restTemplate.getForEntity(anyString(), eq(CandidateDto.class), anyMap())).thenReturn(ResponseEntity.ok(createCandidateDto()));
        when(restTemplate.getForObject(anyString(), eq(CandidateDto[].class))).thenReturn(createCandidates().toArray(new CandidateDto[4]));
        when(restTemplate.getForObject(anyString(), eq(ConstituencyDto[].class)))
            .thenReturn(createConstituencies().toArray(new ConstituencyDto[2]));

        // when
        votingService.secondBallot();

        // then
    }
}