package com.davidmaisuradze.gymapplication.client;

import com.davidmaisuradze.gymapplication.config.feign.FeignConfig;
import com.davidmaisuradze.gymapplication.dto.workload.TrainerWorkloadRequest;
import com.davidmaisuradze.gymapplication.exception.GymException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${svc.workload-svc.name}", configuration = FeignConfig.class)
@Component
public interface WorkloadServiceClient {
    @PostMapping("${svc.workload-svc.update-session-endpoint}")
    @CircuitBreaker(name = "trainerWorkService", fallbackMethod = "fallbackSendWorkload")
    void sendWorkload(@RequestBody TrainerWorkloadRequest trainerWorkloadRequest);

    default void fallbackSendWorkload(TrainerWorkloadRequest trainerWorkloadRequest, Throwable t) {
        if (t instanceof GymException gymException) {
            throw gymException;
        } else if (t instanceof CallNotPermittedException) {
            throw new GymException("CircuitBreaker 'WorkloadServiceClient' does not permit further calls", "503");
        } else {
            throw new GymException("Unknown exception: unable to perform training session modification", "500");
        }
    }
}
