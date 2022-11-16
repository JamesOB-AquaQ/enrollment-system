package com.sega.project.enrollmentsystem.rest.exceptions;

public class MissingFieldException extends RuntimeException {

    public MissingFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingFieldException(String message) {
        super(message);
    }

    public MissingFieldException(Throwable cause) {
        super(cause);
    }

}