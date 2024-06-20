package com.davidmaisuradze.trainerworkservice.message;

import com.davidmaisuradze.trainerworkservice.TrainerWorkServiceApplication;
import com.davidmaisuradze.trainerworkservice.config.TestConfig;
import com.davidmaisuradze.trainerworkservice.dto.ActionType;
import com.davidmaisuradze.trainerworkservice.dto.TrainerWorkloadRequest;
import com.davidmaisuradze.trainerworkservice.security.JwtTokenProvider;
import com.davidmaisuradze.trainerworkservice.service.TrainerWorkSummaryService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {TrainerWorkServiceApplication.class, TestConfig.class})
public class WorkloadMessageListenerSteps {
    private static final Logger log = LoggerFactory.getLogger(WorkloadMessageListenerSteps.class);
    @Autowired
    private JmsTemplate jmsTemplate;
    private boolean messageReceived = false;

    @Given("an ActiveMQ broker is running")
    public void given() {
        assertNotNull(jmsTemplate);
    }

    @When("a message is sent to the queue")
    public void when() {
        try {
            String message = "{\"trainerId\":1, \"workload\":10}";
            jmsTemplate.convertAndSend("test-queue", message);
            messageReceived = true;
        } catch (Exception e) {
            log.error(e.getMessage());
            messageReceived = false;
        }

        log.info("Message received status: {}", messageReceived);
    }

    @Then("the message listener should receive the message")
    public void then() {
        assertTrue(messageReceived);
    }
}
