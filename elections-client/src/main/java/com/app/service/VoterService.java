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
import com.app.model.dto.VoterDto;
import com.app.security.TokenManager;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpMethod.*;

@Service
@RequiredArgsConstructor
public class VoterService {

    private static final String URL_VOTERS = "http://localhost:8080/voters";

    private final RestTemplate restTemplate;
    private final TokenManager tokenManager;

    public VoterDto addVoter(VoterDto voterDto) {
        try {
            Optional.ofNullable(voterDto).orElseThrow(() -> new MyException("Voter is null"));
            HttpEntity<VoterDto> entity = new HttpEntity<>(voterDto, createHeaders());
            ResponseEntity<VoterDto> response = restTemplate.postForEntity(URL_VOTERS, entity, VoterDto.class);

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to add new voter");
        }
    }

    public VoterDto updateVoter(VoterDto voterDto) {
        try {
            Optional.ofNullable(voterDto).orElseThrow(() -> new MyException("Voter is null"));
            HttpEntity<VoterDto> entity = new HttpEntity<>(voterDto, createHeaders());
            ResponseEntity<VoterDto> response = restTemplate.exchange(URL_VOTERS, PUT, entity, VoterDto.class);

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to update voter");
        }
    }

    public VoterDto deleteVoter(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Voter id is null"));
            HttpEntity<VoterDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<VoterDto> response = restTemplate
                .exchange(URL_VOTERS + "/" + id, DELETE, entity, VoterDto.class, createParams(id));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to delete voter by id " + id);
        }
    }

    public VoterDto getOneVoter(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Voter id is null"));
            HttpEntity<VoterDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<VoterDto> response = restTemplate
                .exchange(URL_VOTERS + "/" + id, GET, entity, VoterDto.class, createParams(id));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to find voter by id " + id);
        }
    }

    public List<VoterDto> getAllVoters() {
        try {
            HttpEntity<VoterDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<VoterDto[]> response = restTemplate.exchange(URL_VOTERS, GET, entity, VoterDto[].class);

            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new MyException("Failed to get all voters");
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
