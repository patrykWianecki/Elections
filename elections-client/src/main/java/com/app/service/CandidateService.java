package com.app.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.exceptions.MyException;
import com.app.model.dto.CandidateDto;
import com.app.security.TokenManager;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpMethod.*;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private static final String URL_CANDIDATES = "http://localhost:8080/candidates";

    private final RestTemplate restTemplate;
    private final TokenManager tokenManager;

    public CandidateDto addCandidate(CandidateDto candidateDto) {
        try {
            Optional.ofNullable(candidateDto).orElseThrow(() -> new MyException("Candidate is null"));
            HttpEntity<CandidateDto> entity = new HttpEntity<>(candidateDto, createHeaders());
            ResponseEntity<CandidateDto> response = restTemplate.postForEntity(URL_CANDIDATES, entity, CandidateDto.class);

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to add new candidate");
        }
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

    public CandidateDto deleteCandidate(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Candidate id is null"));
            HttpEntity<CandidateDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<CandidateDto> response = restTemplate
                .exchange(URL_CANDIDATES + "/" + id, DELETE, entity, CandidateDto.class, createParams(id));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to delete candidate by id + " + id);
        }
    }

    public CandidateDto getOneCandidate(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Candidate id is null"));
            HttpEntity<CandidateDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<CandidateDto> response = restTemplate
                .exchange(URL_CANDIDATES + "/" + id, GET, entity, CandidateDto.class, createParams(id));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to find candidate by id " + id);
        }
    }

    public List<CandidateDto> getAllCandidates() {
        try {
            HttpEntity<CandidateDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<CandidateDto[]> response = restTemplate.exchange(URL_CANDIDATES, GET, entity, CandidateDto[].class);

            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new MyException("Failed to get all candidates");
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenManager.getToken());
        return headers;
    }

    private Map<String, String> createParams(final Long id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        return params;
    }
}
