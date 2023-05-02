package com.example.supportportal.config;

import com.example.supportportal.model.User;
import com.example.supportportal.model.UserPrincipal;
import com.example.supportportal.repository.UserRepository;
import com.example.supportportal.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> userByEmail = userRepository.findUserByEmail(username);

            if (!userByEmail.isPresent()) {
                throw new UsernameNotFoundException( String.format("User not found by email %s", username));
            }
            User user = userByEmail.get();

            validateLoginAttempt(user);

            user.setLastLoginDateDisplay(user.getLastLoginDateDisplay());
            user.setLastLoginDate(new Date());

            userRepository.save(user);
            return new UserPrincipal(user);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void validateLoginAttempt(User user)  {
        if (user.isNotLocked()) {
            user.setNotLocked(!loginAttemptService.hasExceededMacAttempts(user.getEmail()));
        }else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getEmail());
        }
    }
}
