package com.davidmaisuradze.gymapplication.metric;

import com.davidmaisuradze.gymapplication.repository.TrainingRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingMetric {
    private final MeterRegistry meterRegistry;
    private final TrainingRepository trainingRepository;

    @PostConstruct
    public void usersMetric() {
        Gauge.builder("custom_trainings_count", trainingRepository, TrainingRepository::count)
                .description("Total number of trainings")
                .register(meterRegistry);
    }
}
