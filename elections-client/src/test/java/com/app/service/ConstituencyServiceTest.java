package com.app.service;

import java.util.List;

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
import com.app.model.dto.ConstituencyDto;
import com.app.security.TokenManager;

import static com.app.builders.MockDataForTests.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;

@ExtendWith(SpringExtension.class)
class ConstituencyServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private RestTemplate restTemplate;
        @MockBean
        private TokenManager tokenManager;

        @Bean
        public ConstituencyService constituencyService() {
            return new ConstituencyService(restTemplate, tokenManager);
        }
    }

    @Autowired
    private ConstituencyService constituencyService;
    @Autowired
    private RestTemplate restTemplate;

    private static ConstituencyDto constituencyDto = createConstituencyDto();

    @Test
    void should_successfully_add_constituency() {
        // given
        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.ok(constituencyDto));

        // when
        ConstituencyDto actualConstituencyDto = constituencyService.addConstituency(createConstituencyDto());

        // then
        assertNotNull(actualConstituencyDto);
        assertEquals(1L, actualConstituencyDto.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualConstituencyDto.getName());
    }

    @Test
    void should_throw_exception_during_adding_null_constituency() {
        // given

        // when + then
        assertThrows(MyException.class, () -> constituencyService.addConstituency(null));
    }

    @Test
    void should_successfully_update_constituency() {
        // given
        when(restTemplate.exchange(anyString(), eq(PUT), any(), eq(ConstituencyDto.class))).thenReturn(ResponseEntity.ok(constituencyDto));

        // when
        ConstituencyDto actualConstituencyDto = constituencyService.updateConstituency(createConstituencyDto());

        // then
        assertNotNull(actualConstituencyDto);
        assertEquals(1L, actualConstituencyDto.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualConstituencyDto.getName());
    }

    @Test
    void should_throw_exception_during_updating_null_constituency() {
        // given

        // when + then
        assertThrows(MyException.class, () -> constituencyService.updateConstituency(null));
    }

    @Test
    void should_successfully_delete_constituency() {
        // given
        when(restTemplate.exchange(anyString(), eq(DELETE), any(), eq(ConstituencyDto.class), anyMap()))
            .thenReturn(ResponseEntity.ok(constituencyDto));

        // when
        ConstituencyDto actualConstituencyDto = constituencyService.deleteConstituency(1L);

        // then
        assertNotNull(actualConstituencyDto);
        assertEquals(1L, actualConstituencyDto.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualConstituencyDto.getName());
    }

    @Test
    void should_throw_exception_during_deleting_constituency_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> constituencyService.deleteConstituency(null));
    }

    @Test
    void should_successfully_find_one_constituency() {
        // given
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(ConstituencyDto.class), anyMap()))
            .thenReturn(ResponseEntity.ok(constituencyDto));

        // when
        ConstituencyDto actualConstituencyDto = constituencyService.getOneConstituency(1L);

        // then
        assertNotNull(actualConstituencyDto);
        assertEquals(1L, actualConstituencyDto.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualConstituencyDto.getName());
    }

    @Test
    void should_throw_exception_during_getting_constituency_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> constituencyService.getOneConstituency(null));
    }

    @Test
    void should_successfully_find_all_constituencies() {
        // given
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(ConstituencyDto[].class)))
            .thenReturn(ResponseEntity.ok(new ConstituencyDto[]{constituencyDto}));

        // when
        List<ConstituencyDto> actualConstituenciesDto = constituencyService.getAllConstituencies();

        // then
        assertNotNull(actualConstituenciesDto);
        assertEquals(1, actualConstituenciesDto.size());

        ConstituencyDto actualConstituencyDto = actualConstituenciesDto.get(0);
        assertNotNull(actualConstituencyDto);
        assertEquals(1L, actualConstituencyDto.getId());
        assertEquals("FIRST_CONSTITUENCY_DTO", actualConstituencyDto.getName());
    }

    @Test
    void should_throw_exception_during_getting_null_constituencies() {
        // given
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(ConstituencyDto[].class))).thenReturn(null);

        // when + then
        assertThrows(MyException.class, () -> constituencyService.getAllConstituencies());
    }
}