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
import com.app.model.dto.security.UserDto;
import com.app.security.TokenManager;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.security.Role.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private RestTemplate restTemplate;
        @MockBean
        private TokenManager tokenManager;

        @Bean
        public UserService userService() {
            return new UserService(restTemplate, tokenManager);
        }
    }

    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;

    private static UserDto userDto = createUserDto();

    @Test
    void should_successfully_add_user() {
        // given
        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.ok(userDto));

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
        when(restTemplate.exchange(anyString(), eq(PUT), any(), eq(UserDto.class))).thenReturn(ResponseEntity.ok(userDto));

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
        when(restTemplate.exchange(anyString(), eq(DELETE), any(), eq(UserDto.class), anyMap())).thenReturn(ResponseEntity.ok(userDto));

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
        when(restTemplate.getForEntity(anyString(), eq(UserDto.class), any(), anyMap())).thenReturn(ResponseEntity.ok(userDto));

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
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(UserDto[].class))).thenReturn(ResponseEntity.ok(new UserDto[]{userDto}));

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
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(UserDto.class))).thenReturn(null);

        // when + then
        assertThrows(MyException.class, () -> userService.getAllUsers());
    }
}