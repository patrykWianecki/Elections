package com.app.validators;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.exceptions.MyException;
import com.app.model.dto.security.UserDto;
import com.app.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            UserDto userDto = (UserDto) target;
            String username = userDto.getUsername();
            String password = userDto.getPassword();
            String email = userDto.getEmail();

            if (StringUtils.isEmpty(username) || !isValid(username)) {
                errors.rejectValue("username", "Login must contain only lower case");
            }
            if (StringUtils.isEmpty(password) || !isValid(password)) {
                errors.rejectValue("password", "Password must contain only lower case");
            }
            if (StringUtils.isEmpty(email) || !isValidEmailAddress(email)) {
                errors.rejectValue("email", "Invalid email format");
            }
            if (!isEmailDuplicate(email)) {
                errors.rejectValue("email", "Email is already in use");
            }
        } catch (Exception e) {
            throw new MyException("User validation exception");
        }
    }

    private boolean isEmailDuplicate(String email) {
        return userService.getAllUsers()
            .stream()
            .noneMatch(userEmail -> email.equals(userEmail.getEmail()));
    }

    private static boolean isValidEmailAddress(String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            return false;
        }
        return true;
    }

    private static boolean isValid(String text) {
        return text.matches("[a-z]+");
    }
}
