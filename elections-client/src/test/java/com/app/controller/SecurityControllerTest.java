package com.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.app.service.SecurityService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SecurityControllerTest {

    @MockBean
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_successfully_login() throws Exception {
        // given

        // when + then
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("login"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("security/loginPage"));
    }

    @Test
    void should_successfully_login_with_post() throws Exception {
        // given

        // when + then
        mockMvc.perform(post("/login"))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("loginPost"))
            .andExpect(view().name("redirect:/"));
    }

    @Test
    void should_successfully_logout() throws Exception {
        // given

        // when + then
        mockMvc.perform(post("/logout"))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("logout"))
            .andExpect(view().name("redirect:/"));
    }

    //    @Test
    //    void should_login_with_error() throws Exception {
    //        // given
    //
    //        // when + then
    //        mockMvc.perform(get("/login/errorPage"))
    //            .andExpect(status().isOk())
    //            .andExpect(handler().methodName("loginError"))
    //            .andExpect(view().name("security/loginPage"));
    //    }

    @Test
    void should_deny_access() throws Exception {
        // given

        // when + then
        mockMvc.perform(get("/accessDenied"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("accessDenied"))
            .andExpect(view().name("security/accessDenied"));
    }
}