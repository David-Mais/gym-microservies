package com.davidmaisuradze.gymapplication.steps;

import com.davidmaisuradze.gymapplication.config.JmsConfig;
import com.davidmaisuradze.gymapplication.dto.workload.ActionType;
import com.davidmaisuradze.gymapplication.dto.workload.TrainerWorkloadRequest;
import com.davidmaisuradze.gymapplication.message.WorkloadMessageProducer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

@SpringBootTest
@CucumberContextConfiguration
@ContextConfiguration(classes = JmsConfig.class)
public class SendWorkloadMessageSteps {
    @Autowired
    private WorkloadMessageProducer workloadMessageProducer;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Given("gym-rest-api is running")
    public void given() {

    }

    @When("a TrainerWorkloadRequest is sent to ActiveMQ")
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
        workloadMessageProducer.sendWorkloadMessage(trainerWorkloadRequest);
    }

    @Then("the message should be in the queue")
    public void then() throws JMSException {
        System.out.println("Waiting to receive message from queue...");
        Message message = jmsTemplate.receive("workload-queue");
        Assertions.assertNotNull(message, "Message should not be null");
        Assertions.assertInstanceOf(TextMessage.class, message, "Message should be of type TextMessage");

        TextMessage textMessage = (TextMessage) message;
        String received = textMessage.getText();
        System.out.println("Received message: " + received);

        Assertions.assertTrue(received.contains("johndoe"), "Message should contain 'johndoe'");
        Assertions.assertTrue(received.contains("John"), "Message should contain 'John'");
        Assertions.assertTrue(received.contains("Doe"), "Message should contain 'Doe'");
    }
}
