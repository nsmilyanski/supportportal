package com.example.supportportal.service;

import com.google.common.cache.LoadingCache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;

    private final LoadingCache<String, Integer> loadingCache;


    public void  evictUserFromLoginAttemptCache(String username) {
        loadingCache.invalidate(username);
    }

    public void addUserToLoginAttemptCache(String username) {
        int attempts = 0;
        try {
            attempts = ATTEMPT_INCREMENT + loadingCache.get(username);
        } catch (ExecutionException e) {
            log.error(e.getMessage());
        }

        loadingCache.put(username, attempts);
    }

    public boolean hasExceededMacAttempts(String username) {
        try {
            return loadingCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPTS;
        } catch (ExecutionException e) {
            log.error(e.getMessage());
        }
        return false;
    }

}
