package com.davidmaisuradze.gymapplication.metric;

import com.davidmaisuradze.gymapplication.repository.UserRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersMetric {
    private final MeterRegistry meterRegistry;
    private final UserRepository userRepository;

    @PostConstruct
    public void usersMetric() {
        Gauge.builder("custom_user_count", userRepository, UserRepository::count)
                .description("Total number of users")
                .register(meterRegistry);
    }
}
