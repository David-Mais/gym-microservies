package com.davidmaisuradze.trainerworkservice.config.logging;

import com.davidmaisuradze.trainerworkservice.dto.TrainerWorkloadRequest;
import jakarta.jms.Message;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut(value = "execution(* com.davidmaisuradze.trainerworkservice.message..*(..)) && args(request, message)", argNames = "request,message")
    public void loggableMethods(TrainerWorkloadRequest request, Message message) {}

    @Before(value = "loggableMethods(request, message)", argNames = "request,message")
    public void logBefore(TrainerWorkloadRequest request, Message message) throws Exception {
        String transactionId = message.getStringProperty("transactionId");
        log.info("TransactionId: {}, message read by WorkloadMessageListener", transactionId);
    }

    @AfterThrowing(pointcut = "loggableMethods(request, message)", throwing = "e", argNames = "request,message,e")
    public void logAfterThrowing(TrainerWorkloadRequest request, Message message, Exception e) {
        log.error("Error processing message: {}", e.getMessage());
    }
}