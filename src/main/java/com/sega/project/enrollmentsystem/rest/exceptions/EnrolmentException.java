package com.sega.project.enrollmentsystem.rest.exceptions;

public class EnrolmentException extends RuntimeException {

    public EnrolmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnrolmentException(String message) {
        super(message);
    }

    public EnrolmentException(Throwable cause) {
        super(cause);
    }

}