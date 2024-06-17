package com.davidmaisuradze.gymapplication.message;

import com.davidmaisuradze.gymapplication.dto.workload.TrainerWorkloadRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkloadMessageProducer {
    @Value("${messaging.jms.destination}")
    private String destination;
    private final JmsTemplate jmsTemplate;

    public void sendWorkloadMessage(TrainerWorkloadRequest request) {
        String transactionId = MDC.get("transactionId");
        String jwtToken = extractJwtToken();

        jmsTemplate.convertAndSend(destination, request, message -> {
            message.setStringProperty("_type", "TrainerWorkloadRequest");

            if (jwtToken != null) {
                message.setStringProperty("Authorization", "Bearer " + jwtToken);
            }
            if (transactionId != null) {
                message.setStringProperty("transactionId", transactionId);
            }

            return message;
        });
    }

    private String extractJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String string) {
            return string;
        }
        return null;
    }
}
