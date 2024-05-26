package com.davidmaisuradze.gymapplication.message;

import com.davidmaisuradze.gymapplication.dto.workload.TrainerWorkloadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkloadMessageSender {
    @Value("${messaging.jms.destination}")
    private String destination;
    private final JmsTemplate jmsTemplate;

    public void sendWorkloadMessage(TrainerWorkloadRequest request) {
        jmsTemplate.convertAndSend(destination, request, message -> {
            message.setStringProperty("_type", "TrainerWorkloadRequest");
            return message;
        });
    }
}
