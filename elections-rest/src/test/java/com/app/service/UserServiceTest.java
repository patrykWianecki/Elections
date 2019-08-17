package com.app.service;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.exceptions.MyException;
import com.app.model.dto.security.UserDto;
import com.app.repository.UserRepository;
import com.app.repository.VerificationTokenRepository;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.security.Role.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private PasswordEncoder passwordEncoder;
        @MockBean
        private ApplicationEventPublisher eventPublisher;
        @MockBean
        private UserRepository userRepository;
        @MockBean
        private VerificationTokenRepository tokenRepository;
        @MockBean
        private ToolsService toolsService;

        @Bean
        public UserService userService() {
            return new UserService(passwordEncoder, eventPublisher, userRepository, tokenRepository, toolsService);
        }
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private ToolsService toolsService;
    @Autowired
    private UserService userService;

    @Test
    void should_successfully_add_user() {
        // given
        when(userRepository.save(any())).thenReturn(createUser());

        // when
        UserDto actualUserDto = userService.addUser(createUserDto());

        // then
        assertNotNull(actualUserDto);
        assertEquals(1L, actualUserDto.getId());
        assertEquals("username", actualUserDto.getUsername());
        assertEquals("password", actualUserDto.getPassword());
        assertEquals(ADMIN, actualUserDto.getRole());
        assertEquals("user@gmail.com", actualUserDto.getEmail());
        assertEquals(true, actualUserDto.getEnabled());
    }

    @Test
    void should_throw_exception_during_adding_null_user() {
        // given

        // when + then
        assertThrows(MyException.class, () -> userService.addUser(null));
    }

    @Test
    void should_successfully_update_user() {
        // given
        when(userRepository.save(any())).thenReturn(createUser());
        when(toolsService.findUserByIdWithErrorCheck(anyLong())).thenReturn(createUser());

        // when
        UserDto actualUserDto = userService.updateUser(createUserDto());

        // then
        assertNotNull(actualUserDto);
        assertEquals(1L, actualUserDto.getId());
        assertEquals("username", actualUserDto.getUsername());
        assertEquals("password", actualUserDto.getPassword());
        assertEquals(ADMIN, actualUserDto.getRole());
        assertEquals("user@gmail.com", actualUserDto.getEmail());
        assertEquals(true, actualUserDto.getEnabled());
    }

    @Test
    void should_throw_exception_during_updating_null_user() {
        // given

        // when + then
        assertThrows(MyException.class, () -> userService.updateUser(null));
    }

    @Test
    void should_successfully_delete_user() {
        // given
        when(toolsService.findUserByIdWithErrorCheck(anyLong())).thenReturn(createUser());

        // when
        UserDto actualUserDto = userService.deleteUser(1L);

        // then
        assertNotNull(actualUserDto);
        assertEquals(1L, actualUserDto.getId());
        assertEquals("username", actualUserDto.getUsername());
        assertEquals("password", actualUserDto.getPassword());
        assertEquals(ADMIN, actualUserDto.getRole());
        assertEquals("user@gmail.com", actualUserDto.getEmail());
        assertEquals(true, actualUserDto.getEnabled());
    }

    @Test
    void should_throw_exception_during_deleting_user_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> userService.deleteUser(null));
    }

    @Test
    void should_successfully_find_one_user() {
        // given
        when(toolsService.findUserByIdWithErrorCheck(anyLong())).thenReturn(createUser());

        // when
        UserDto actualUserDto = userService.getOneUser(1L);

        // then
        assertNotNull(actualUserDto);
        assertEquals(1L, actualUserDto.getId());
        assertEquals("username", actualUserDto.getUsername());
        assertEquals("password", actualUserDto.getPassword());
        assertEquals(ADMIN, actualUserDto.getRole());
        assertEquals("user@gmail.com", actualUserDto.getEmail());
        assertEquals(true, actualUserDto.getEnabled());
    }

    @Test
    void should_throw_exception_during_getting_user_with_null_id() {
        // given

        // when + then
        assertThrows(MyException.class, () -> userService.getOneUser(null));
    }

    @Test
    void should_successfully_find_users() {
        // given
        when(userRepository.findAll()).thenReturn(Collections.singletonList(createUser()));

        // when
        List<UserDto> actualUsersDto = userService.getAllUsers();

        // then
        assertNotNull(actualUsersDto);
        assertEquals(1, actualUsersDto.size());

        UserDto actualUserDto = actualUsersDto.get(0);
        assertNotNull(actualUserDto);
        assertEquals(1L, actualUserDto.getId());
        assertEquals("username", actualUserDto.getUsername());
        assertEquals("password", actualUserDto.getPassword());
        assertEquals(ADMIN, actualUserDto.getRole());
        assertEquals("user@gmail.com", actualUserDto.getEmail());
        assertEquals(true, actualUserDto.getEnabled());
    }

    @Test
    void should_throw_exception_during_getting_null_users() {
        // given
        when(userRepository.findAll()).thenReturn(null);

        // when + then
        assertThrows(MyException.class, () -> userService.getAllUsers());
    }

    @Test
    void should_successfully_create_verification_token() {
        // given

        // when
        userService.createVerificationToken(createUser(), "TOKEN");

        // then
        verify(verificationTokenRepository, times(1)).save(any());
    }

    @Test
    void should_throw_exception_during_creating_verification_token_with_null_user() {
        // given

        // when + then
        assertThrows(MyException.class, () -> userService.createVerificationToken(null, "TOKEN"));
    }
}