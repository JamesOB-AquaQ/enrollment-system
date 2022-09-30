package com.sega.project.enrollmentsystem.rest;

import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.jdbc.StudentJdbcDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
