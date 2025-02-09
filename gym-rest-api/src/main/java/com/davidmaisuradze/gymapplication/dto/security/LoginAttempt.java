package com.davidmaisuradze.gymapplication.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginAttempt {
    private String username;
    private int failedAttempts;
    private LocalDateTime lockoutTime;
}
