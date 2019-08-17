package com.app.service;

import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.exceptions.MyException;
import com.app.model.dto.security.UserDto;
import com.app.security.TokenManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private static final String URL_LOGIN = "http://localhost:8080/login";

    private final RestTemplate restTemplate;
    private final TokenManager tokenManager;

    public void login(UserDto user) {
        Optional.ofNullable(user).orElseThrow(() -> new MyException("User is null"));
        HttpEntity<UserDto> entity = new HttpEntity<>(user);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(URL_LOGIN, entity, String.class);

        tokenManager.setToken(loginResponse.getBody());
    }

    public void logout() {
        tokenManager.setToken("");
    }
}
