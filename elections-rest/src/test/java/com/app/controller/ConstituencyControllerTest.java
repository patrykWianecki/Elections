package com.app.controller;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.app.model.dto.ConstituencyDto;
import com.app.service.ConstituencyService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.app.builders.MockDataForTests.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ConstituencyControllerTest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String BODY = "{\"username\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}";

    @MockBean
    private ConstituencyService constituencyService;

    @Autowired
    private MockMvc mockMvc;

    private ConstituencyDto constituencyDto = createConstituencyDto();
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .content(BODY))
            .andExpect(status().isOk()).andReturn();

        token = result.getResponse().getContentAsString();
    }

    @Test
    void should_successfully_add_constituency() throws Exception {
        // given
        when(constituencyService.addConstituency(any())).thenReturn(constituencyDto);

        // when + then
        mockMvc.perform(post("/constituencies")
            .header("Authorization", "Bearer " + token)
            .content(toJson(constituencyDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.name", equalTo("FIRST_CONSTITUENCY_DTO")))
            .andExpect(content().json("{id:1,name:FIRST_CONSTITUENCY_DTO}"));
    }

    @Test
    void should_successfully_update_constituency() throws Exception {
        // given
        when(constituencyService.updateConstituency(any())).thenReturn(constituencyDto);

        // when + then
        mockMvc.perform(put("/constituencies")
            .header("Authorization", "Bearer " + token)
            .content(toJson(constituencyDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.name", equalTo("FIRST_CONSTITUENCY_DTO")))
            .andExpect(content().json("{id:1,name:FIRST_CONSTITUENCY_DTO}"));
    }

    @Test
    void should_successfully_delete_constituency_by_id() throws Exception {
        // given
        when(constituencyService.deleteConstituency(anyLong())).thenReturn(constituencyDto);

        // when + then
        mockMvc.perform(delete("/constituencies/{id}", 1)
            .header("Authorization", "Bearer " + token)
            .content(toJson(constituencyDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.name", equalTo("FIRST_CONSTITUENCY_DTO")))
            .andExpect(content().json("{id:1,name:FIRST_CONSTITUENCY_DTO}"));
    }

    @Test
    void should_successfully_find_one_constituency() throws Exception {
        // given
        when(constituencyService.getOneConstituency(anyLong())).thenReturn(constituencyDto);

        // when + then
        mockMvc.perform(get("/constituencies/{id}", 1)
            .header("Authorization", "Bearer " + token)
            .content(toJson(constituencyDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.name", equalTo("FIRST_CONSTITUENCY_DTO")))
            .andExpect(content().json("{id:1,name:FIRST_CONSTITUENCY_DTO}"));
    }

    @Test
    void should_successfully_find_all_constituencies() throws Exception {
        // given
        when(constituencyService.getAllConstituencies()).thenReturn(Collections.singletonList(constituencyDto));

        // when + then
        mockMvc.perform(get("/constituencies")
            .header("Authorization", "Bearer " + token)
            .content(toJson(constituencyDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", equalTo(1)))
            .andExpect(jsonPath("$[0].name", equalTo("FIRST_CONSTITUENCY_DTO")))
            .andExpect(content().json("[{id:1,name:FIRST_CONSTITUENCY_DTO}]"));
    }

    private static String toJson(ConstituencyDto constituencyDto) {
        try {
            return new ObjectMapper().writeValueAsString(constituencyDto);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create json object");
        }
    }
}