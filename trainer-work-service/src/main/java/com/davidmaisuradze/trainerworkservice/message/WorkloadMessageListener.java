package com.davidmaisuradze.trainerworkservice.message;

import com.davidmaisuradze.trainerworkservice.dto.TrainerWorkloadRequest;
import com.davidmaisuradze.trainerworkservice.service.TrainerWorkSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkloadMessageListener {
    private final TrainerWorkSummaryService trainerWorkSummaryService;
    @JmsListener(destination = "${messaging.jms.destination}")
    public void receiveWorkload(TrainerWorkloadRequest request) {
        trainerWorkSummaryService.addWorkload(request);
    }
}
