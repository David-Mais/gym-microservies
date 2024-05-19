package com.davidmaisuradze.gymapplication.healthcheck;

import com.davidmaisuradze.gymapplication.util.DetailsGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DetailsGeneratorHealthIndicator implements HealthIndicator {
    private final DetailsGenerator detailsGenerator;

    @Override
    public Health health() {
        try {
            String username = detailsGenerator.generateUsername("First", "Last");
            String password = detailsGenerator.generatePassword();

            boolean working = validateService(username, password);

            if (working) {
                return Health.up()
                        .withDetail("status", "service is working")
                        .withDetail("username", username)
                        .withDetail("password", password)
                        .build();
            } else {
                return Health.down()
                        .withDetail("status", "service is not working")
                        .withDetail("username", "N/A")
                        .withDetail("password", "N/A")
                        .build();
            }
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }

    private boolean validateService(String username, String password) {
        return username != null && password != null;
    }
}
