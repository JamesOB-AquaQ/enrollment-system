package com.sega.project.enrollmentsystem.rest;

import com.sega.project.enrollmentsystem.jdbc.EnrolmentJdbcDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EnrolmentController {

    @Autowired
    EnrolmentJdbcDAO enrolmentRepo;


    @PostMapping("students/{studentId}/enroll/{courseId}")
    public ResponseEntity<String> enrollCourse(@PathVariable int studentId, @PathVariable int courseId) {
        enrolmentRepo.enrollStudentInCourse(studentId, courseId);
        String body = "Student with id: " + studentId + " was enrolled in course with id: " + courseId;
        return new ResponseEntity<>(body, HttpStatus.OK);

    }

    @DeleteMapping("students/{studentId}/unenroll/{courseId}")
    public ResponseEntity<String> unenrollCourse(@PathVariable int studentId, @PathVariable int courseId) {
        enrolmentRepo.removeStudentFromCourse(studentId, courseId);
        String body = "Student with id: " + studentId + " was unenrolled from course with id: " + courseId;
        return new ResponseEntity<>(body, HttpStatus.OK);

    }


    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(EntityNotFoundException exc) {

        CustomErrorResponse error = new CustomErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(InsufficientCreditsException exc) {

        CustomErrorResponse error = new CustomErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(MaxCapacityException exc) {

        CustomErrorResponse error = new CustomErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(IllegalArgumentException exc) {

        CustomErrorResponse error = new CustomErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(Exception exc) {

        CustomErrorResponse error = new CustomErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
