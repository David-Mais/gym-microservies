package com.davidmaisuradze.gymapplication.healthcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "custom")
@RequiredArgsConstructor
public class CustomEndpoint {
    private final DatabaseHealthIndicator databaseHealthIndicator;
    private final DetailsGeneratorHealthIndicator detailsGeneratorHealthIndicator;
    private final MemoryHealthIndicator memoryHealthIndicator;

    @ReadOperation
    public Health health() {
        Health.Builder builder = Health.up();

        builder.withDetail("database", databaseHealthIndicator.health());
        builder.withDetail("generator", detailsGeneratorHealthIndicator.health());
        builder.withDetail("memory", memoryHealthIndicator.health());

        return builder.build();
    }
}
