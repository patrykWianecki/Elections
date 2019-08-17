package com.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.app.model.dto.security.UserDto;
import com.app.security.TokenManager;

import static com.app.builders.MockDataForTests.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class SecurityServiceTest {

    private static final String TOKEN = "TOKEN";

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private RestTemplate restTemplate;
        @MockBean
        private TokenManager tokenManager;

        @Bean
        public SecurityService securityService() {
            return new SecurityService(restTemplate, tokenManager);
        }
    }

    @Autowired
    private SecurityService securityService;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private RestTemplate restTemplate;

    private static UserDto userDto = createUserDto();

    @Test
    void should_successfully_login() {
        // given
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(ResponseEntity.ok(TOKEN));

        // when
        securityService.login(userDto);

        // then
        verify(tokenManager, times(1)).setToken(TOKEN);
    }

    @Test
    void should_successfully_logout() {
        // given + when
        securityService.logout();

        // then
        verify(tokenManager, times(1)).setToken("");
    }
}