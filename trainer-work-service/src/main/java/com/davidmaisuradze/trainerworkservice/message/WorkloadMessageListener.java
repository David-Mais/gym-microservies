package com.davidmaisuradze.trainerworkservice.message;

import com.davidmaisuradze.trainerworkservice.dto.TrainerWorkloadRequest;
import com.davidmaisuradze.trainerworkservice.exception.WorkloadException;
import com.davidmaisuradze.trainerworkservice.security.JwtTokenProvider;
import com.davidmaisuradze.trainerworkservice.service.TrainerWorkSummaryService;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkloadMessageListener {
    private final TrainerWorkSummaryService trainerWorkSummaryService;
    private final JwtTokenProvider jwtTokenProvider;

    @JmsListener(destination = "${messaging.jms.destinations.main}")
    public void receiveWorkload(TrainerWorkloadRequest request, Message message) {
        extractAndValidateJwtToken(request, message);
    }

    private void extractAndValidateJwtToken(TrainerWorkloadRequest request, Message message) {
        try {
            String jwtToken = message.getStringProperty("Authorization");
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                jwtToken = jwtToken.substring(7);
                if (jwtTokenProvider.isTokenExpired(jwtToken)) {
                    trainerWorkSummaryService.addWorkload(request);
                } else {
                    throw new WorkloadException("JWT token not valid", "401");
                }
            } else {
                throw new WorkloadException("JWT token not present or not starting with Bearer", "401");
            }
        } catch (Exception e) {
            throw new WorkloadException("Error receiving workload message", "500");
        }
    }
}
