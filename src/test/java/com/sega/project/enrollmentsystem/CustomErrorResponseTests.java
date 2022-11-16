package com.sega.project.enrollmentsystem;

import com.sega.project.enrollmentsystem.rest.CustomErrorResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomErrorResponseTests {

    @Test
    public void testCustomErrorResponseConstructor() {
        int status = 200;
        String message = "message";
        long timeStamp = 123456789;
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(status, message, timeStamp);
        assertEquals(status, customErrorResponse.getStatus());
        assertEquals(message, customErrorResponse.getMessage());
        assertEquals(timeStamp, customErrorResponse.getTimeStamp());
    }

}
