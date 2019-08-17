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
import com.app.model.dto.ConstituencyDto;
import com.app.security.TokenManager;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpMethod.*;

@Service
@RequiredArgsConstructor
public class ConstituencyService {

    private static final String URL_CONSTITUENCIES = "http://localhost:8080/constituencies";

    private final RestTemplate restTemplate;
    private final TokenManager tokenManager;

    public ConstituencyDto addConstituency(ConstituencyDto constituencyDto) {
        try {
            Optional.ofNullable(constituencyDto).orElseThrow(() -> new MyException("Constituency is null"));
            HttpEntity<ConstituencyDto> entity = new HttpEntity<>(constituencyDto, createHeaders());
            ResponseEntity<ConstituencyDto> response = restTemplate.postForEntity(URL_CONSTITUENCIES, entity, ConstituencyDto.class);

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to add new constituency");
        }
    }

    public ConstituencyDto updateConstituency(ConstituencyDto constituencyDto) {
        try {
            Optional.ofNullable(constituencyDto).orElseThrow(() -> new MyException("Constituency is null"));
            HttpEntity<ConstituencyDto> entity = new HttpEntity<>(constituencyDto, createHeaders());
            ResponseEntity<ConstituencyDto> response = restTemplate.exchange(URL_CONSTITUENCIES, PUT, entity, ConstituencyDto.class);

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to update constituency");
        }
    }

    public ConstituencyDto deleteConstituency(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Constituency id is null"));
            HttpEntity<ConstituencyDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<ConstituencyDto> response = restTemplate
                .exchange(URL_CONSTITUENCIES + "/" + id, DELETE, entity, ConstituencyDto.class, createParams(id));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to delete constituency by id " + id);
        }
    }

    public ConstituencyDto getOneConstituency(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Constituency id is null"));
            HttpEntity<ConstituencyDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<ConstituencyDto> response = restTemplate
                .exchange(URL_CONSTITUENCIES + "/" + id, GET, entity, ConstituencyDto.class, createParams(id));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("Failed to find constituency by id " + id);
        }
    }

    public List<ConstituencyDto> getAllConstituencies() {
        try {
            HttpEntity<ConstituencyDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<ConstituencyDto[]> response = restTemplate.exchange(URL_CONSTITUENCIES, GET, entity, ConstituencyDto[].class);

            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new MyException("Failed to get all constituencies");
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
