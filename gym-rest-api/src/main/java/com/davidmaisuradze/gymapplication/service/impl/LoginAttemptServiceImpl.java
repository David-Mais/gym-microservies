package com.davidmaisuradze.gymapplication.service.impl;

import com.davidmaisuradze.gymapplication.dto.security.LoginAttempt;
import com.davidmaisuradze.gymapplication.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {
    @Value("${security.login.max-attempts}")
    private int maxAttempts;
    @Value("${security.login.timeout}")
    private int timeoutSeconds;
    private final ConcurrentHashMap<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        LoginAttempt attempt = loginAttempts
                .getOrDefault(
                        username,
                        new LoginAttempt(username, 0, null)
                );
        attempt.setFailedAttempts(attempt.getFailedAttempts() + 1);
        if (attempt.getFailedAttempts() >= maxAttempts) {
            attempt.setLockoutTime(LocalDateTime.now());
        }
        loginAttempts.put(username, attempt);
    }

    public void loginSucceeded(String username) {
        LoginAttempt attempt = loginAttempts.getOrDefault(username, null);
        if (attempt != null) {
            attempt.setFailedAttempts(0);
            attempt.setLockoutTime(null);
            loginAttempts.put(username, attempt);
        }
    }

    public boolean isLockedOut(String username) {
        LoginAttempt attempt = loginAttempts.getOrDefault(username, null);
        if (attempt != null && attempt.getLockoutTime() != null) {
            Duration duration = Duration.ofSeconds(timeoutSeconds);
            return LocalDateTime.now().isBefore(attempt.getLockoutTime().plus(duration));
        }
        return false;
    }
}
