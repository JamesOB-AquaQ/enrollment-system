package com.sega.project.enrollmentsystem.rest;

public class InsufficientCreditsException extends RuntimeException {

    public InsufficientCreditsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientCreditsException(String message) {
        super(message);
    }

    public InsufficientCreditsException(Throwable cause) {
        super(cause);
    }

}