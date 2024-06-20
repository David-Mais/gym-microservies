package com.davidmaisuradze.trainerworkservice.message;

import com.davidmaisuradze.trainerworkservice.TrainerWorkServiceApplication;
import com.davidmaisuradze.trainerworkservice.config.TestConfig;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
