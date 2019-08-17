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

import com.app.model.dto.CandidateDto;
import com.app.service.CandidateService;
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
class CandidateControllerTest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String BODY = "{\"username\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}";

    @MockBean
    private CandidateService candidateService;

    @Autowired
    private MockMvc mockMvc;

    private CandidateDto candidateDto = createCandidateDto();
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .content(BODY))
            .andExpect(status().isOk()).andReturn();

        token = result.getResponse().getContentAsString();
    }

    @Test
    void should_successfully_add_candidate() throws Exception {
        // given
        when(candidateService.addCandidate(any())).thenReturn(candidateDto);

        // when + then
        mockMvc.perform(post("/candidates")
            .header("Authorization", "Bearer " + token)
            .content(toJson(candidateDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.name", equalTo("Name")))
            .andExpect(jsonPath("$.surname", equalTo("Surname")))
            .andExpect(jsonPath("$.age", equalTo(20)))
            .andExpect(jsonPath("$.gender", equalTo("MALE")))
            .andExpect(jsonPath("$.isValid", equalTo(true)))
            .andExpect(jsonPath("$.votes", equalTo(10)))
            .andExpect(content().json("{id:1,name:Name,surname:Surname,age:20,gender:MALE,isValid:true,votes:10}"));
    }

    @Test
    void should_successfully_update_candidate() throws Exception {
        // given
        when(candidateService.updateCandidate(any())).thenReturn(candidateDto);

        // when + then
        mockMvc.perform(put("/candidates")
            .header("Authorization", "Bearer " + token)
            .content(toJson(candidateDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.name", equalTo("Name")))
            .andExpect(jsonPath("$.surname", equalTo("Surname")))
            .andExpect(jsonPath("$.age", equalTo(20)))
            .andExpect(jsonPath("$.gender", equalTo("MALE")))
            .andExpect(jsonPath("$.isValid", equalTo(true)))
            .andExpect(jsonPath("$.votes", equalTo(10)))
            .andExpect(content().json("{id:1,name:Name,surname:Surname,age:20,gender:MALE,isValid:true,votes:10}"));
    }

    @Test
    void should_successfully_delete_candidate_by_id() throws Exception {
        // given
        when(candidateService.deleteCandidate(anyLong())).thenReturn(candidateDto);

        // when + then
        mockMvc.perform(delete("/candidates/{id}", 1)
            .header("Authorization", "Bearer " + token)
            .content(toJson(candidateDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.name", equalTo("Name")))
            .andExpect(jsonPath("$.surname", equalTo("Surname")))
            .andExpect(jsonPath("$.age", equalTo(20)))
            .andExpect(jsonPath("$.gender", equalTo("MALE")))
            .andExpect(jsonPath("$.isValid", equalTo(true)))
            .andExpect(jsonPath("$.votes", equalTo(10)))
            .andExpect(content().json("{id:1,name:Name,surname:Surname,age:20,gender:MALE,isValid:true,votes:10}"));
    }

    @Test
    void should_successfully_find_one_candidate() throws Exception {
        // given
        when(candidateService.getOneCandidate(anyLong())).thenReturn(candidateDto);

        // when + then
        mockMvc.perform(get("/candidates/{id}", 1)
            .header("Authorization", "Bearer " + token)
            .content(toJson(candidateDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.name", equalTo("Name")))
            .andExpect(jsonPath("$.surname", equalTo("Surname")))
            .andExpect(jsonPath("$.age", equalTo(20)))
            .andExpect(jsonPath("$.gender", equalTo("MALE")))
            .andExpect(jsonPath("$.isValid", equalTo(true)))
            .andExpect(jsonPath("$.votes", equalTo(10)))
            .andExpect(content().json("{id:1,name:Name,surname:Surname,age:20,gender:MALE,isValid:true,votes:10}"));
    }

    @Test
    void should_successfully_find_all_candidates() throws Exception {
        // given
        when(candidateService.getAllCandidates()).thenReturn(Collections.singletonList(candidateDto));

        // when + then
        mockMvc.perform(get("/candidates")
            .header("Authorization", "Bearer " + token)
            .content(toJson(candidateDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", equalTo(1)))
            .andExpect(jsonPath("$[0].name", equalTo("Name")))
            .andExpect(jsonPath("$[0].surname", equalTo("Surname")))
            .andExpect(jsonPath("$[0].age", equalTo(20)))
            .andExpect(jsonPath("$[0].gender", equalTo("MALE")))
            .andExpect(jsonPath("$[0].isValid", equalTo(true)))
            .andExpect(jsonPath("$[0].votes", equalTo(10)))
            .andExpect(content().json("[{id:1,name:Name,surname:Surname,age:20,gender:MALE,isValid:true,votes:10}]"));
    }

    private static String toJson(CandidateDto candidateDto) {
        try {
            return new ObjectMapper().writeValueAsString(candidateDto);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create json object");
        }
    }
}