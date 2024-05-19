package com.davidmaisuradze.gymapplication.exception;

import lombok.Getter;

@Getter
public class GymException extends RuntimeException{
    private final String errorCode;
    public GymException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
