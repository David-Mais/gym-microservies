package com.davidmaisuradze.gymapplication.healthcheck;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MemoryHealthIndicator implements HealthIndicator {
    private static final String BYTES = " bytes";

    @Override
    public Health health() {
        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        double freePercentage = ((double) freeMemory / (double) totalMemory) * 100;

        if (freePercentage > 25) {
            return Health.up()
                    .withDetail("free_memory", freeMemory + BYTES)
                    .withDetail("total_memory", totalMemory + BYTES)
                    .withDetail("free_memory_percentage", freePercentage + "%")
                    .build();
        } else {
            return Health.down()
                    .withDetail("free_memory", freeMemory + BYTES)
                    .withDetail("total_memory", totalMemory + BYTES)
                    .withDetail("free_memory_percentage", freePercentage + "%")
                    .build();
        }
    }
}
