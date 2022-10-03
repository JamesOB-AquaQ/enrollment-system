package com.sega.project.enrollmentsystem.rest;

import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.jdbc.StudentJdbcDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    StudentJdbcDAO studentRepo;

    @GetMapping("/students")
    public List<Student> getStudents(){
        return studentRepo.findAll();
    }
    @GetMapping("/students/{studentId}")
    public Student getStudentById(@PathVariable int studentId){
        if(studentRepo.findById(studentId)==null){
            throw new StudentNotFoundException("Student id not found - "+studentId);
        }
        System.out.println("Result: "+studentRepo.findById(10));
        return studentRepo.findById(studentId);
    }
    @GetMapping("/students/name")
    public List<Student> getStudentsByName(@RequestParam String forename, @RequestParam String surname){
        return studentRepo.findByName(forename,surname);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {

        StudentErrorResponse error = new StudentErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
