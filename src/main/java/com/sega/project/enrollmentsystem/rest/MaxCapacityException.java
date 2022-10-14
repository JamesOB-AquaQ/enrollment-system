package com.sega.project.enrollmentsystem.rest;

public class MaxCapacityException extends RuntimeException {

    public MaxCapacityException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxCapacityException(String message) {
        super(message);
    }

    public MaxCapacityException(Throwable cause) {
        super(cause);
    }

}