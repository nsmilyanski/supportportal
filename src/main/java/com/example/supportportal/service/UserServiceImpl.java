package com.example.supportportal.service;

import com.example.supportportal.model.User;
import com.example.supportportal.model.UserPrincipal;
import com.example.supportportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByUsername = userRepository.findUserByUsername(username);
        if (userByUsername == null) {
            String message = String.format("User not found by username %s", username);
            log.error(message);
            throw new UsernameNotFoundException(message);
        }
        userByUsername.setLastLoginDateDisplay(userByUsername.getLastLoginDateDisplay());
        userByUsername.setLastLoginDate(new Date());
        userRepository.save(userByUsername);
        log.info("Returning found user by username: " + username);
        return userByUsername;
    }
}
