package com.app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.exceptions.MyException;
import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.VoterDto;
import com.app.model.dto.security.UserDto;
import com.app.security.TokenManager;

import static org.springframework.http.HttpMethod.*;

@Service
public class VotingService {

    private final static String URL_CANDIDATES = "http://localhost:8080/candidates";
    private final static String URL_CONSTITUENCIES = "http://localhost:8080/constituencies";
    private final static String URL_VOTERS = "http://localhost:8080/voters";
    private final static String URL_TOKENS = "http://localhost:8080/votersTokens";
    private static final String URL_LOGIN = "http://localhost:8080/login";

    private RestTemplate restTemplate;
    private TokenManager tokenManager;

    public VotingService(final RestTemplate restTemplate, final TokenManager tokenManager) {
        this.restTemplate = restTemplate;
        this.tokenManager = tokenManager;
        login();
    }

    public CandidateDto updateCandidate(CandidateDto candidateDto) {
        try {
            Optional.ofNullable(candidateDto).orElseThrow(() -> new MyException("Candidate is null"));
            HttpEntity<CandidateDto> entity = new HttpEntity<>(candidateDto, createHeaders());
            ResponseEntity<CandidateDto> response = restTemplate.exchange(URL_CANDIDATES, PUT, entity, CandidateDto.class);

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to update candidate");
        }
    }

    public CandidateDto findCandidateById(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Candidate id is null"));
            HttpEntity<CandidateDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<CandidateDto> response = restTemplate
                .exchange(URL_CANDIDATES + "/" + id, GET, entity, CandidateDto.class, createParams("id", String.valueOf(id)));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to find candidate by id " + id);
        }
    }

    public String removeUsedToken(Integer token) {
        try {
            Optional.ofNullable(token).orElseThrow(() -> new MyException("Voter token is null"));
            HttpEntity<String> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<String> response = restTemplate
                .exchange(URL_TOKENS + "/" + token, DELETE, entity, String.class, createParams("token", String.valueOf(token)));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to remove used token");
        }
    }

    public VoterDto findVoterByToken(Integer voterToken) {
        try {
            Optional.ofNullable(voterToken).orElseThrow(() -> new MyException("Voter token is null"));
            HttpEntity<VoterDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<VoterDto> response = restTemplate
                .exchange(URL_TOKENS + "/" + voterToken, GET, entity, VoterDto.class, createParams("token", String.valueOf(voterToken)));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to find voter by given token");
        }
    }

    private List<CandidateDto> findAllCandidates() {
        try {
            HttpEntity<CandidateDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<CandidateDto[]> response = restTemplate.exchange(URL_CANDIDATES, GET, entity, CandidateDto[].class);

            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new MyException("Failed to get all candidates");
        }
    }

    private List<ConstituencyDto> findAllConstituencies() {
        try {
            HttpEntity<ConstituencyDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<ConstituencyDto[]> response = restTemplate.exchange(URL_CONSTITUENCIES, GET, entity, ConstituencyDto[].class);

            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new MyException("Failed to get all constituencies");
        }
    }

    private List<VoterDto> findAllVoters() {
        try {
            HttpEntity<VoterDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<VoterDto[]> response = restTemplate.exchange(URL_VOTERS, GET, entity, VoterDto[].class);

            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new MyException("Failed to get all voters");
        }
    }

    private String deleteAllVoters() {
        try {
            HttpEntity<VoterDto> entity = new HttpEntity<>(null, createHeaders());
            restTemplate.exchange(URL_VOTERS, DELETE, entity, String.class);

            return "Voters deleted successfully";
        } catch (Exception e) {
            throw new MyException("Failed to delete all voters");
        }
    }

    public List<CandidateDto> findAllCandidatesForVoterWithGivenToken(Integer token) {
        try {
            Optional.ofNullable(token).orElseThrow(() -> new MyException("Token is null"));
            VoterDto voterDto = findVoterByToken(token);

            return findCandidatesByConstituencyId(voterDto.getConstituencyDto().getId());
        } catch (Exception e) {
            throw new MyException("Failed to collect all candidates in constituency by voter token");
        }
    }

    private List<CandidateDto> findCandidatesByConstituencyId(Long constituencyId) {
        Optional.ofNullable(constituencyId).orElseThrow(() -> new MyException("Constituency id is null"));

        return findAllCandidates()
            .stream()
            .filter(candidate -> Objects.nonNull(candidate.getConstituencyDto()))
            .filter(candidate -> Objects.nonNull(candidate.getConstituencyDto().getId()))
            .filter(candidate -> constituencyId.equals(candidate.getConstituencyDto().getId()))
            .filter(CandidateDto::getIsValid)
            .collect(Collectors.toList());
    }

    public List<CandidateDto> findWinnersInEachConstituency() {
        try {
            return new ArrayList<>(findAllCandidates()
                .stream()
                .sorted(Comparator.comparing(CandidateDto::getVotes, Comparator.reverseOrder()))
                .filter(candidate -> Objects.nonNull(candidate.getConstituencyDto()))
                .filter(candidate -> Objects.nonNull(candidate.getConstituencyDto().getName()))
                .collect(Collectors.toMap(
                    constituency -> constituency.getConstituencyDto().getName(),
                    candidate -> candidate,
                    (v1, v2) -> v1,
                    LinkedHashMap::new))
                .values());
        } catch (Exception e) {
            throw new MyException("Failed to find winners from all constituencies");
        }
    }

    public double countAverageVotersAge() {
        try {
            return findAllVoters()
                .stream()
                .map(VoterDto::getAge)
                .mapToDouble(Integer::doubleValue)
                .reduce(0, Double::sum) / (double) findAllVoters().size();
        } catch (Exception e) {
            throw new MyException("Failed to count average voters age");
        }
    }

    public Map<String, BigDecimal> countGenderPercentage() {
        try {
            return countAmountOfEachGender()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    percent -> countPercentage(percent.getValue(), findAllVoters().size()))
                );
        } catch (Exception e) {
            throw new MyException("Failed to count gender percentage");
        }
    }

    private static BigDecimal countPercentage(Long numberOfVotes, int amountOfAllVotes) {
        return BigDecimal.valueOf(numberOfVotes)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(amountOfAllVotes), 2, RoundingMode.HALF_UP);
    }

    private Map<String, Long> countAmountOfEachGender() {
        return collectAllGenders()
            .stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private List<String> collectAllGenders() {
        return findAllVoters()
            .stream()
            .map(voter -> voter.getGender().toString())
            .collect(Collectors.toList());
    }

    public Map<ConstituencyDto, Map<CandidateDto, BigDecimal>> groupCandidatesWithPercentageResultsInConstituencies() {
        try {
            return findAllConstituencies()
                .stream()
                .collect(Collectors.toMap(
                    constituency -> constituency,
                    candidateResult -> percentPerCandidate()
                        .entrySet()
                        .stream()
                        .filter(candidate -> candidateResult.getId().equals(candidate.getKey().getConstituencyDto().getId()))
                        .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue))
                    )
                );
        } catch (Exception e) {
            throw new MyException("Failed to count candidates percentage results in constituencies");
        }
    }

    private Map<CandidateDto, BigDecimal> percentPerCandidate() {
        Map<CandidateDto, BigDecimal> percentPerCandidate = new LinkedHashMap<>();

        countVotesInAllConstituencies()
            .forEach((key, value) -> findAllCandidates()
                .forEach(candidate -> {
                    if (key.equals(candidate.getConstituencyDto().getId())) {
                        BigDecimal result = countPercentage(Long.valueOf(candidate.getVotes()), value.intValue());
                        percentPerCandidate.put(candidate, result);
                    }
                }));

        return percentPerCandidate;
    }

    private Map<Long, Long> countVotesInAllConstituencies() {
        return findAllCandidates()
            .stream()
            .collect(Collectors.toMap(
                candidateConstituencyId -> candidateConstituencyId.getConstituencyDto().getId(),
                votesInConstituencies -> findAllCandidates()
                    .stream()
                    .filter(candidate -> candidate.getConstituencyDto().getId().equals(votesInConstituencies.getConstituencyDto().getId()))
                    .map(votes -> (long) votes.getVotes())
                    .collect(Collectors.toList()),
                (v1, v2) -> v1,
                LinkedHashMap::new))
            .entrySet()
            .stream()
            .sorted(Comparator.comparing(Map.Entry::getKey))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                votesListPerConstituency -> votesListPerConstituency.getValue()
                    .stream()
                    .reduce(0L, Long::sum),
                (v1, v2) -> v1,
                LinkedHashMap::new));
    }

    public void secondBallot() {
        try {
            findAllConstituencies().forEach(constituency -> {
                List<CandidateDto> candidates = sortCandidatesByNumberOfVotes(constituency.getId());
                Integer allVotes = countAllVotesInParticularConstituency(candidates);
                Map<CandidateDto, BigDecimal> votesPercentPerCandidate = countVotesPercentPerCandidate(candidates, allVotes);
                BigDecimal compare = new ArrayList<>(votesPercentPerCandidate.values()).get(0);

                if (compare.compareTo(BigDecimal.valueOf(50)) > 0) {
                    candidates.forEach(candidate -> {
                        if (!candidates.get(0).getId().equals(candidate.getId())) {
                            setCandidateValid(candidate.getId());
                        }
                    });
                } else {
                    setInvalidStatusForCandidateNotQualifiedToSecondBallot(votesPercentPerCandidate);
                }

                deleteAllVoters();
            });
        } catch (Exception e) {
            throw new MyException("Failed to make second ballot");
        }
    }

    private void setInvalidStatusForCandidateNotQualifiedToSecondBallot(Map<CandidateDto, BigDecimal> votesPercentPerCandidate) {
        candidatesNotQualifiedToSecondBallot(votesPercentPerCandidate).forEach(candidate -> setCandidateValid(candidate.getId()));
    }

    private void setCandidateValid(Long candidateId) {
        Optional.ofNullable(candidateId).orElseThrow(() -> new MyException("Candidate id is null"));
        CandidateDto candidateToUpdate = findCandidateById(candidateId);
        candidateToUpdate.setIsValid(false);
        updateCandidate(candidateToUpdate);
    }

    private List<CandidateDto> sortCandidatesByNumberOfVotes(Long constituencyId) {
        Optional.ofNullable(constituencyId).orElseThrow(() -> new MyException("Constituency id is null"));

        return findAllCandidates()
            .stream()
            .filter(candidate -> constituencyId.equals(candidate.getConstituencyDto().getId()))
            .sorted(Comparator.comparing(CandidateDto::getVotes, Comparator.reverseOrder()))
            .collect(Collectors.toList());
    }

    private Map<CandidateDto, BigDecimal> countVotesPercentPerCandidate(List<CandidateDto> candidates, Integer votesSum) {
        Optional.ofNullable(votesSum).orElseThrow(() -> new MyException("Votes sum is null"));
        Optional.ofNullable(candidates).orElseThrow(() -> new MyException("Candidates are null"));

        return candidates
            .stream()
            .collect(Collectors.toMap(
                candidate -> candidate,
                candidatePercent -> countPercentage(Long.valueOf(candidatePercent.getVotes()), votesSum)));
    }

    private Integer countAllVotesInParticularConstituency(List<CandidateDto> candidates) {
        Optional.ofNullable(candidates).orElseThrow(() -> new MyException("Candidates are null"));

        return candidates
            .stream()
            .map(CandidateDto::getVotes)
            .reduce(0, Integer::sum);
    }

    private List<CandidateDto> candidatesNotQualifiedToSecondBallot(Map<CandidateDto, BigDecimal> candidatesToFilter) {
        Optional.ofNullable(candidatesToFilter).orElseThrow(() -> new MyException("Candidates are null"));
        final BigDecimal percentageOfSecondCandidate = new ArrayList<>(candidatesToFilter.values()).get(1);

        return candidatesToFilter.entrySet()
            .stream()
            .sorted((percent1, percent2) -> percent2.getValue().compareTo(percent1.getValue()))
            .filter(percent -> percent.getValue().compareTo(percentageOfSecondCandidate) < 0)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenManager.getToken());
        return headers;
    }

    private Map<String, String> createParams(String param, String value) {
        Map<String, String> params = new HashMap<>();
        params.put(param, value);
        return params;
    }

    private void login() {
        UserDto user = UserDto.builder().username("admin").password("admin").build();
        HttpEntity<UserDto> entity = new HttpEntity<>(user);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(URL_LOGIN, entity, String.class);
        tokenManager.setToken(loginResponse.getBody());
    }
}
