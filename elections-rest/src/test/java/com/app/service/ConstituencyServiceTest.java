package com.app.service;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.exceptions.MyException;
import com.app.model.dto.ConstituencyDto;
import com.app.repository.ConstituencyRepository;

import static com.app.builders.MockDataForTests.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ConstituencyServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private ConstituencyRepository constituencyRepository;
        @MockBean
        private ToolsService toolsService;

        @Bean
        public ConstituencyService constituencyService() {
            return new ConstituencyService(constituencyRepository, toolsService);
        }
    }

    @Autowired
    private ConstituencyRepository constituencyRepository;
    @Autowired
    private ConstituencyService constituencyService;
    @Autowired
    private ToolsService toolsService;

    @Test
    void should_successfully_add_constituency() {
        // given
        when(constituencyRepository.save(any())).thenReturn(createConstituency());

        // when
        ConstituencyDto actualConstituencyDto = constituencyService.addConstituency(createConstituencyDto());

        // then
        assertNotNull(actualConstituencyDto);
        assertEquals(1L, actualConstituencyDto.getId());
        assertEquals("FIRST_CONSTITUENCY", actualConstituencyDto.getName());
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
        when(constituencyRepository.save(any())).thenReturn(createConstituency());
        when(toolsService.findConstituencyByIdWithErrorCheck(anyLong())).thenReturn(createConstituency());

        // when
        ConstituencyDto actualConstituencyDto = constituencyService.updateConstituency(createConstituencyDto());

        // then
        assertNotNull(actualConstituencyDto);
        assertEquals(1L, actualConstituencyDto.getId());
        assertEquals("FIRST_CONSTITUENCY", actualConstituencyDto.getName());
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
        when(toolsService.findConstituencyByIdWithErrorCheck(anyLong())).thenReturn(createConstituency());

        // when
        ConstituencyDto actualConstituencyDto = constituencyService.deleteConstituency(1L);

        // then
        assertNotNull(actualConstituencyDto);
        assertEquals(1L, actualConstituencyDto.getId());
        assertEquals("FIRST_CONSTITUENCY", actualConstituencyDto.getName());
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
        when(toolsService.findConstituencyByIdWithErrorCheck(anyLong())).thenReturn(createConstituency());

        // when
        ConstituencyDto actualConstituencyDto = constituencyService.getOneConstituency(1L);

        // then
        assertNotNull(actualConstituencyDto);
        assertEquals(1L, actualConstituencyDto.getId());
        assertEquals("FIRST_CONSTITUENCY", actualConstituencyDto.getName());
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
        when(constituencyRepository.findAll()).thenReturn(Collections.singletonList(createConstituency()));

        // when
        List<ConstituencyDto> actualConstituenciesDto = constituencyService.getAllConstituencies();

        // then
        assertNotNull(actualConstituenciesDto);
        assertEquals(1, actualConstituenciesDto.size());

        ConstituencyDto actualConstituencyDto = actualConstituenciesDto.get(0);
        assertNotNull(actualConstituencyDto);
        assertEquals(1L, actualConstituencyDto.getId());
        assertEquals("FIRST_CONSTITUENCY", actualConstituencyDto.getName());
    }

    @Test
    void should_throw_exception_during_getting_null_constituencies() {
        // given
        when(constituencyRepository.findAll()).thenReturn(null);

        // when + then
        assertThrows(MyException.class, () -> constituencyService.getAllConstituencies());
    }
}