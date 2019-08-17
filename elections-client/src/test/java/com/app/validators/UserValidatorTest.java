package com.app.validators;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.app.exceptions.MyException;
import com.app.model.dto.security.UserDto;
import com.app.service.UserService;

import static com.app.builders.MockDataForTests.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserValidatorTest {

    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private UserService userService;

        @Bean
        public UserValidator userValidator() {
            return new UserValidator(userService);
        }
    }

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        assertTrue(userValidator.supports(UserDto.class));
    }

    @Test
    void should_successfully_valid_user() {
        // given
        UserDto validUser = createValidUser();
        Errors errors = new BeanPropertyBindingResult(validUser, "validUser");
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(createInvalidUser()));

        // when
        userValidator.validate(validUser, errors);

        // then
        assertFalse(errors.hasErrors());
    }

    @Test
    void should_find_errors_during_validating_user() {
        // given
        UserDto invalidUser = createInvalidUser();
        Errors errors = new BeanPropertyBindingResult(invalidUser, "invalidUser");
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(invalidUser));

        // when
        userValidator.validate(invalidUser, errors);

        // then
        assertTrue(errors.hasErrors());

        FieldError username = errors.getFieldError("username");
        assertNotNull(username);
        assertEquals("Login must contain only lower case", username.getCode());

        FieldError password = errors.getFieldError("password");
        assertNotNull(password);
        assertEquals("Password must contain only lower case", password.getCode());

        FieldError email = errors.getFieldError("email");
        assertNotNull(email);
        assertEquals("Invalid email format", email.getCode());
    }

    @Test
    void should_throw_exception_when_user_is_null() {
        // given

        // when + then
        assertThrows(MyException.class, () -> userValidator.validate(null, null));
    }
}