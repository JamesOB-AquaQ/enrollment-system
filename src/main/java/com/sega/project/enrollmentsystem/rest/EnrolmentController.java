package com.sega.project.enrollmentsystem.rest;

import com.sega.project.enrollmentsystem.dto.CourseDTO;
import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.jdbc.CourseJdbcDAO;
import com.sega.project.enrollmentsystem.jdbc.EnrolmentJdbcDAO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EnrolmentController {

    @Autowired
    EnrolmentJdbcDAO courseRepo;
    @Autowired()
    ModelMapper modelMapper;

    @PostMapping("students/{studentId}/enroll/{courseId}")
    public ResponseEntity<String> enrollCourse(@PathVariable int studentId, @PathVariable int courseId) {
        int enrolled = courseRepo.enrollStudentInCourse(studentId, courseId);
        if (enrolled == 0) {
            throw new EntityNotFoundException("Student with id: " + studentId+" was not enrolled in course with id: "+courseId);
        }
        String body = "Student with id: " + studentId+" was enrolled in course with id: "+courseId;
        return new ResponseEntity<>(body, HttpStatus.OK);
    }





    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(EntityNotFoundException exc) {

        CustomErrorResponse error = new CustomErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    } @ExceptionHandler
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

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(BindException exc) {

        CustomErrorResponse error = new CustomErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
