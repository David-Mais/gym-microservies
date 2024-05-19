package com.davidmaisuradze.gymapplication.util;

import com.davidmaisuradze.gymapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DetailsGenerator {
    private final UserRepository userRepository;

    public String generateUsername(String firstName, String lastName) {
        List<String> usernames = userRepository.findAllUsernames();

        String baseUsername = firstName + "." + lastName;
        String finalUsername = baseUsername;
        int counter = 0;

        while (usernames.contains(finalUsername)) {
            counter++;
            finalUsername = baseUsername + counter;
        }

        log.info("Username generated: {}", finalUsername);
        return finalUsername;
    }

    public String generatePassword() {
        log.info("Random password generated");
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
