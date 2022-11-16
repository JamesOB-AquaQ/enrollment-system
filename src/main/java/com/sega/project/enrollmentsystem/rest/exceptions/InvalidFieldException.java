package com.sega.project.enrollmentsystem.rest.exceptions;

public class InvalidFieldException extends RuntimeException {

    public InvalidFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFieldException(String message) {
        super(message);
    }

    public InvalidFieldException(Throwable cause) {
        super(cause);
    }

}