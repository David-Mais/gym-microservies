package com.davidmaisuradze.trainerworkservice.exception;

import lombok.Getter;

@Getter
public class WorkloadException extends RuntimeException{
    private final String errorCode;
    public WorkloadException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
