package com.app.controller;

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

import com.app.model.dto.VoterDto;
import com.app.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.Education.*;
import static com.app.model.Gender.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TokenControllerTest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String BODY = "{\"username\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}";
    private static final Integer VOTER_ID = 1;
    private static final Integer VOTER_AGE = 25;
    private static final String VOTER_GENDER = MALE.name();
    private static final String VOTER_EDUCATION = HIGHER_EDUCATION.name();
    private static final String VOTER_JSON = "{id:1,age:25,gender:MALE,education:HIGHER_EDUCATION}";
    private static final String SUCCESSFUL_DELETED_TOKEN_MESSAGE = "Successfully deleted token";

    @MockBean
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;

    private VoterDto voterDto = createVoterDto();
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .content(BODY))
            .andExpect(status().isOk()).andReturn();

        token = result.getResponse().getContentAsString();
    }

    @Test
    void should_successfully_find_voter_by_token() throws Exception {
        when(tokenService.findVoterByToken(anyInt())).thenReturn(voterDto);

        // when + then
        mockMvc.perform(get("/votersTokens/{token}", 1)
            .header("Authorization", "Bearer " + token)
            .content(toJson(voterDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(VOTER_ID)))
            .andExpect(jsonPath("$.age", equalTo(VOTER_AGE)))
            .andExpect(jsonPath("$.gender", equalTo(VOTER_GENDER)))
            .andExpect(jsonPath("$.education", equalTo(VOTER_EDUCATION)))
            .andExpect(jsonPath("$.tokenDto", notNullValue()))
            .andExpect(jsonPath("$.constituencyDto", notNullValue()))
            .andExpect(content().json(VOTER_JSON));
    }

    @Test
    void should_successfully_delete_voter_by_token() throws Exception {
        // given
        when(tokenService.deleteUsedToken(anyInt())).thenReturn(SUCCESSFUL_DELETED_TOKEN_MESSAGE);

        // when + then
        mockMvc.perform(delete("/votersTokens/{voterToken}", 1)
            .header("Authorization", "Bearer " + token)
            .content(toJson(voterDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(SUCCESSFUL_DELETED_TOKEN_MESSAGE));
    }

    private static String toJson(VoterDto voterDto) {
        try {
            return new ObjectMapper().writeValueAsString(voterDto);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create json object");
        }
    }
}