package com.davidmaisuradze.gymapplication.service;

public interface LoginAttemptService {
    void loginFailed(String username);
    void loginSucceeded(String username);
    boolean isLockedOut(String username);
}
