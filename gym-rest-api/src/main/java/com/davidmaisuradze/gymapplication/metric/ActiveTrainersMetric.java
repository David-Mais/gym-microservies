package com.davidmaisuradze.gymapplication.metric;

import com.davidmaisuradze.gymapplication.repository.TrainerRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveTrainersMetric {
    private final MeterRegistry meterRegistry;
    private final TrainerRepository trainerRepository;

    @PostConstruct
    public void init() {
        Gauge.builder("custom_active_trainers_count", trainerRepository, TrainerRepository::countActiveTrainers)
                .description("Total number of active trainers")
                .register(meterRegistry);
    }
}
