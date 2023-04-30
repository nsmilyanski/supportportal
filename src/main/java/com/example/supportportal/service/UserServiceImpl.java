package com.example.supportportal.service;

import com.example.supportportal.enumeration.Role;
import com.example.supportportal.exception.domain.EmailExistException;
import com.example.supportportal.exception.domain.UserNotFoundException;
import com.example.supportportal.exception.domain.UsernameExistException;
import com.example.supportportal.model.User;
import com.example.supportportal.model.UserPrincipal;
import com.example.supportportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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

    @Override
    public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        user.setUserId(generateuserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user.setFirstNam(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setAcrive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImgUrl(getTemporaryProfileImageUrl());

        userRepository.save(user);
        log.info("New user password: " + password);
        return null;
    }

    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile/temp").toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateuserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException("No user find by username: " + currentUsername);
            }
            User userByUsername = findUserByUsername(currentUsername);
            if (userByUsername != null && !currentUser.getId().equals(userByUsername.getId())) {
                throw new UsernameExistException("Username already exist!");
            }
            User userByEmail = findUserByUsername(currentUsername);
            if (userByEmail != null && !currentUser.getId().equals(userByEmail.getId())) {
                throw new EmailExistException("Email already exist!");
            }
            return currentUser;
        }else {
            User userByUsername = findUserByUsername(currentUsername);
            if (userByUsername != null) {
                throw new UsernameExistException("Username already exist!");
            }
            User userByEmail = findUserByUsername(currentUsername);
            if (userByEmail != null) {
                throw new EmailExistException("Email already exist!");
            }
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }
}
