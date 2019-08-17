package com.app.service;

import com.app.exceptions.MyException;
import com.app.model.dto.ModelMapper;
import com.app.model.dto.security.UserDto;
import com.app.model.security.User;
import com.app.model.security.VerificationToken;
import com.app.repository.UserRepository;
import com.app.repository.VerificationTokenRepository;
import com.app.security.events.OnRegisterData;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.app.model.dto.ModelMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private static final String USER_URL = "http://localhost:8080/users";

    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final ToolsService toolsService;

    public UserDto addUser(UserDto userDto) {
        try {
            Optional.ofNullable(userDto).orElseThrow(() -> new MyException("UserDto is null"));
            User user = fromUserDtoToUser(userDto);
            user.setEnabled(false);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            eventPublisher.publishEvent(new OnRegisterData(user, USER_URL));

            return fromUserToUserDto(userRepository.save(user));
        } catch (Exception e) {
            throw new MyException("Failed to add new user");
        }
    }

    public UserDto updateUser(UserDto userDto) {
        try {
            Optional.ofNullable(userDto).orElseThrow(() -> new MyException("UserDto is null"));
            User user = createUser(userDto);

            return fromUserToUserDto(userRepository.save(user));
        } catch (Exception e) {
            throw new MyException("Failed to update user");
        }
    }

    public UserDto deleteUser(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("UserDto id is null"));
            User user = toolsService.findUserByIdWithErrorCheck(id);
            userRepository.deleteById(id);

            return fromUserToUserDto(user);
        } catch (Exception e) {
            throw new MyException("Failed to delete user by id " + id);
        }
    }

    public UserDto getOneUser(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("UserDto id is null"));
            User user = toolsService.findUserByIdWithErrorCheck(id);

            return fromUserToUserDto(user);
        } catch (Exception e) {
            throw new MyException("Failed to find one user by id " + id);
        }
    }

    public List<UserDto> getAllUsers() {
        try {
            return userRepository.findAll()
                .stream()
                .filter(user -> Objects.nonNull(user.getId()))
                .sorted(Comparator.comparing(User::getId))
                .map(ModelMapper::fromUserToUserDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException("Failed to find all users");
        }
    }

    public void createVerificationToken(User user, String token) {
        try {
            Optional.ofNullable(user).orElseThrow(() -> new MyException("User is null"));
            Optional.ofNullable(token).orElseThrow(() -> new MyException("Token is null"));
            tokenRepository.save(VerificationToken.builder()
                .token(token)
                .user(user)
                .expirationDateTime(LocalDateTime.now().plusMinutes(5))
                .build()
            );
        } catch (Exception e) {
            throw new MyException("Failed to create verification token");
        }
    }

    private User createUser(final UserDto userDto) {
        User user = toolsService.findUserByIdWithErrorCheck(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setEnabled(userDto.getEnabled());
        user.setRole(userDto.getRole());

        return user;
    }
}
