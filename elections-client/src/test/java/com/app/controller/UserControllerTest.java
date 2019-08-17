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

import com.app.model.dto.security.UserDto;
import com.app.service.UserService;
import com.app.validators.UserValidator;

import static com.app.builders.MockDataForTests.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private UserValidator userValidator;

    @Autowired
    private MockMvc mockMvc;

    private UserDto userDto = createUserDto();

    @Test
    void should_successfully_add_user() throws Exception {
        // given
        when(userValidator.supports(any())).thenReturn(true);
        when(userService.addUser(any())).thenReturn(userDto);

        // when
        mockMvc.perform(get("/users/add"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("addUser"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("users/add"));
    }

    @Test
    void should_successfully_add_user_with_post() throws Exception {
        // given
        when(userValidator.supports(any())).thenReturn(true);
        when(userService.addUser(any())).thenReturn(userDto);

        // when
        mockMvc.perform(post("/users/add"))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("addUserPost"))
            .andExpect(view().name("redirect:/users"));
    }

    @Test
    void should_successfully_update_user() throws Exception {
        // given
        when(userValidator.supports(any())).thenReturn(true);
        when(userService.getOneUser(any())).thenReturn(userDto);

        // when
        mockMvc.perform(get("/users/edit/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("updateUser"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("users/edit"));
    }

    @Test
    void should_successfully_update_user_with_post() throws Exception {
        // given
        when(userValidator.supports(any())).thenReturn(true);
        when(userService.updateUser(any())).thenReturn(userDto);

        // when
        mockMvc.perform(post("/users/edit"))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("updateUserPost"))
            .andExpect(view().name("redirect:/users"));
    }

    @Test
    void should_successfully_delete_user_by_id() throws Exception {
        // given
        when(userService.deleteUser(anyLong())).thenReturn(userDto);

        // when
        mockMvc.perform(post("/users/delete?id={id}", 1))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("deleteUser"))
            .andExpect(view().name("redirect:/users"));
    }

    @Test
    void should_successfully_find_one_user() throws Exception {
        // given
        when(userValidator.supports(any())).thenReturn(true);
        when(userService.getOneUser(anyLong())).thenReturn(userDto);

        // when
        mockMvc.perform(get("/users/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("showOneUser"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("users/one"));
    }

    @Test
    void should_successfully_find_all_users() throws Exception {
        // given
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDto));

        // when
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("getAllUsers"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("users/all"));
    }
}