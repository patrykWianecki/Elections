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
import com.app.model.dto.security.UserDto;
import com.app.security.TokenManager;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpMethod.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String URL_USERS = "http://localhost:8080/users";

    private final RestTemplate restTemplate;
    private final TokenManager tokenManager;

    public UserDto addUser(UserDto userDto) {
        try {
            Optional.ofNullable(userDto).orElseThrow(() -> new MyException("User is null"));
            HttpEntity<UserDto> entity = new HttpEntity<>(userDto, createHeaders());
            ResponseEntity<UserDto> response = restTemplate.postForEntity(URL_USERS, entity, UserDto.class);

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to add new user");
        }
    }

    public UserDto updateUser(UserDto userDto) {
        try {
            Optional.ofNullable(userDto).orElseThrow(() -> new MyException("User is null"));
            HttpEntity<UserDto> entity = new HttpEntity<>(userDto, createHeaders());
            ResponseEntity<UserDto> response = restTemplate.exchange(URL_USERS, PUT, entity, UserDto.class);

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to update user");
        }
    }

    public UserDto deleteUser(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("User id is null"));
            HttpEntity<ConstituencyDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<UserDto> response = restTemplate.exchange(URL_USERS + "/" + id, DELETE, entity, UserDto.class, createParams(id));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to delete user by id " + id);
        }
    }

    public UserDto getOneUser(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("User id is null"));
            HttpEntity<ConstituencyDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<UserDto> response = restTemplate.getForEntity(URL_USERS + "/" + id, UserDto.class, entity, createParams(id));

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new MyException("Missing response body"));
        } catch (Exception e) {
            throw new MyException("Failed to find user by id " + id);
        }
    }

    public List<UserDto> getAllUsers() {
        try {
            HttpEntity<UserDto> entity = new HttpEntity<>(null, createHeaders());
            ResponseEntity<UserDto[]> response = restTemplate.exchange(URL_USERS, GET, entity, UserDto[].class);

            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new MyException("Failed to get all users");
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
