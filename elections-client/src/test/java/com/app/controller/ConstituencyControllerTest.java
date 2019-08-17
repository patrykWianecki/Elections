package com.app.controller;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.app.model.dto.ConstituencyDto;
import com.app.service.ConstituencyService;
import com.app.validators.ConstituencyValidator;

import static com.app.builders.MockDataForTests.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ConstituencyControllerTest {

    @MockBean
    private ConstituencyService constituencyService;
    @MockBean
    private ConstituencyValidator constituencyValidator;

    @Autowired
    private MockMvc mockMvc;

    private ConstituencyDto constituencyDto = createConstituencyDto();

    @Test
    void should_successfully_add_constituency() throws Exception {
        // given
        when(constituencyValidator.supports(any())).thenReturn(true);
        when(constituencyService.addConstituency(any())).thenReturn(constituencyDto);

        // when
        mockMvc.perform(get("/constituencies/add"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("addConstituency"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("constituencies/add"));
    }

    @Test
    void should_successfully_add_constituency_with_post() throws Exception {
        // given
        when(constituencyValidator.supports(any())).thenReturn(true);
        when(constituencyService.addConstituency(any())).thenReturn(constituencyDto);

        // when
        mockMvc.perform(post("/constituencies/add"))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("addConstituencyPost"))
            .andExpect(view().name("redirect:/constituencies"));
    }

    @Test
    void should_successfully_update_constituency() throws Exception {
        // given
        when(constituencyValidator.supports(any())).thenReturn(true);
        when(constituencyService.getOneConstituency(any())).thenReturn(constituencyDto);

        // when
        mockMvc.perform(get("/constituencies/edit/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("editConstituency"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("constituencies/edit"));
    }

    @Test
    void should_successfully_update_constituency_with_post() throws Exception {
        // given
        when(constituencyValidator.supports(any())).thenReturn(true);
        when(constituencyService.updateConstituency(any())).thenReturn(constituencyDto);

        // when
        mockMvc.perform(post("/constituencies/edit"))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("editConstituenciesPost"))
            .andExpect(view().name("redirect:/constituencies"));
    }

    @Test
    void should_successfully_delete_constituency_by_id() throws Exception {
        // given
        when(constituencyService.deleteConstituency(anyLong())).thenReturn(constituencyDto);

        // when
        mockMvc.perform(post("/constituencies/delete?id={id}", 1))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("deleteConstituency"))
            .andExpect(view().name("redirect:/constituencies"));
    }

    @Test
    void should_successfully_find_one_constituency() throws Exception {
        // given
        when(constituencyValidator.supports(any())).thenReturn(true);
        when(constituencyService.getOneConstituency(anyLong())).thenReturn(constituencyDto);

        // when
        mockMvc.perform(get("/constituencies/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("showOneConstituency"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("constituencies/one"));
    }

    @Test
    void should_successfully_find_all_constituencies() throws Exception {
        // given
        when(constituencyService.getAllConstituencies()).thenReturn(Collections.singletonList(constituencyDto));

        // when
        mockMvc.perform(get("/constituencies"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("showAllConstituencies"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("constituencies/all"));
    }
}