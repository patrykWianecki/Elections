package com.app.security;

import com.app.exceptions.MyException;
import com.app.model.security.User;
import com.app.repository.UserRepository;
import com.app.service.ToolsService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MyException("Missing user with username " + username));

            return createUser(user);
        } catch (Exception e) {
            throw new MyException("Failed to load user by username");
        }
    }

    private static org.springframework.security.core.userdetails.User createUser(User user) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
            true, true, true, true, Authorities.getAuthorities(user.getRole())
        );
    }
}
