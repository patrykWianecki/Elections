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

import com.app.model.dto.security.UserDto;
import com.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.security.Role.*;
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
class UserControllerTest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String BODY = "{\"username\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}";
    private static final Integer USER_ID = 1;
    private static final String USER_USERNAME = "username";
    private static final String USER_PASSWORD = "password";
    private static final String USER_ROLE = ADMIN.name();
    private static final String USER_EMAIL = "user@gmail.com";
    private static final boolean USER_ENABLED = true;
    private static final String USER_JSON = "{id:1,username:username,password:password,role:ADMIN,email:user@gmail.com,enabled:true}";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private UserDto userDto = createUserDto();
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .content(BODY))
            .andExpect(status().isOk()).andReturn();

        token = result.getResponse().getContentAsString();
    }

    @Test
    void should_successfully_add_user() throws Exception {
        // given
        when(userService.addUser(any())).thenReturn(userDto);

        // when + then
        mockMvc.perform(post("/users")
            .header("Authorization", "Bearer " + token)
            .content(toJson(userDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(USER_ID)))
            .andExpect(jsonPath("$.username", equalTo(USER_USERNAME)))
            .andExpect(jsonPath("$.password", equalTo(USER_PASSWORD)))
            .andExpect(jsonPath("$.role", equalTo(USER_ROLE)))
            .andExpect(jsonPath("$.email", equalTo(USER_EMAIL)))
            .andExpect(jsonPath("$.enabled", equalTo(USER_ENABLED)))
            .andExpect(content().json(USER_JSON));
    }

    @Test
    void should_successfully_update_user() throws Exception {
        // given
        when(userService.updateUser(any())).thenReturn(userDto);

        // when + then
        mockMvc.perform(put("/users")
            .header("Authorization", "Bearer " + token)
            .content(toJson(userDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(USER_ID)))
            .andExpect(jsonPath("$.username", equalTo(USER_USERNAME)))
            .andExpect(jsonPath("$.password", equalTo(USER_PASSWORD)))
            .andExpect(jsonPath("$.role", equalTo(USER_ROLE)))
            .andExpect(jsonPath("$.email", equalTo(USER_EMAIL)))
            .andExpect(jsonPath("$.enabled", equalTo(USER_ENABLED)))
            .andExpect(content().json(USER_JSON));
    }

    @Test
    void should_successfully_delete_user_by_id() throws Exception {
        // given
        when(userService.deleteUser(anyLong())).thenReturn(userDto);

        // when + then
        mockMvc.perform(delete("/users/{id}", 1)
            .header("Authorization", "Bearer " + token)
            .content(toJson(userDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(USER_ID)))
            .andExpect(jsonPath("$.username", equalTo(USER_USERNAME)))
            .andExpect(jsonPath("$.password", equalTo(USER_PASSWORD)))
            .andExpect(jsonPath("$.role", equalTo(USER_ROLE)))
            .andExpect(jsonPath("$.email", equalTo(USER_EMAIL)))
            .andExpect(jsonPath("$.enabled", equalTo(USER_ENABLED)))
            .andExpect(content().json(USER_JSON));
    }

    @Test
    void should_successfully_find_one_user() throws Exception {
        // given
        when(userService.getOneUser(anyLong())).thenReturn(userDto);

        // when + then
        mockMvc.perform(get("/users/{id}", 1)
            .header("Authorization", "Bearer " + token)
            .content(toJson(userDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(USER_ID)))
            .andExpect(jsonPath("$.username", equalTo(USER_USERNAME)))
            .andExpect(jsonPath("$.password", equalTo(USER_PASSWORD)))
            .andExpect(jsonPath("$.role", equalTo(USER_ROLE)))
            .andExpect(jsonPath("$.email", equalTo(USER_EMAIL)))
            .andExpect(jsonPath("$.enabled", equalTo(USER_ENABLED)))
            .andExpect(content().json(USER_JSON));
    }

    @Test
    void should_successfully_find_all_users() throws Exception {
        // given
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDto));

        // when + then
        mockMvc.perform(get("/users")
            .header("Authorization", "Bearer " + token)
            .content(toJson(userDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", equalTo(USER_ID)))
            .andExpect(jsonPath("$[0].username", equalTo(USER_USERNAME)))
            .andExpect(jsonPath("$[0].password", equalTo(USER_PASSWORD)))
            .andExpect(jsonPath("$[0].role", equalTo(USER_ROLE)))
            .andExpect(jsonPath("$[0].email", equalTo(USER_EMAIL)))
            .andExpect(jsonPath("$[0].enabled", equalTo(USER_ENABLED)))
            .andExpect(content().json("[" + USER_JSON + "]"));
    }

    private static String toJson(UserDto userDto) {
        try {
            return new ObjectMapper().writeValueAsString(userDto);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create json object");
        }
    }
}