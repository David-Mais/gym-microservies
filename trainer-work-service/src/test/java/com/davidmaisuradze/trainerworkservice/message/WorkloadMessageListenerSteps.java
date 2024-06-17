package com.davidmaisuradze.trainerworkservice.message;

import com.davidmaisuradze.trainerworkservice.dto.ActionType;
import com.davidmaisuradze.trainerworkservice.dto.TrainerWorkloadRequest;
import com.davidmaisuradze.trainerworkservice.security.JwtTokenProvider;
import com.davidmaisuradze.trainerworkservice.service.TrainerWorkSummaryService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@CucumberContextConfiguration
public class WorkloadMessageListenerSteps {
    @Autowired
    private WorkloadMessageListener workloadMessageListener;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private TrainerWorkSummaryService trainerWorkSummaryService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private ActiveMQTextMessage textMessage;

    @Given("an ActiveMQ broker is running")
    public void given() {
    }

    @When("a message is sent to the queue")
    public void when() {
        TrainerWorkloadRequest trainerWorkloadRequest = TrainerWorkloadRequest.builder()
                .username("johndoe")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .trainingDate(LocalDate.now())
                .durationMinutes(60)
                .actionType(ActionType.ADD)
                .build();

        jmsTemplate.convertAndSend("workload-queue", trainerWorkloadRequest, message -> {
            message.setStringProperty("Authorization", "Bearer valid-jwt-token");
            return message;
        });
    }

    @Then("the message listener should receive the message")
    public void then() {
        verify(jwtTokenProvider, times(1)).isTokenExpired(anyString());
        verify(trainerWorkSummaryService, times(1)).addWorkload(any(TrainerWorkloadRequest.class));
    }
}
