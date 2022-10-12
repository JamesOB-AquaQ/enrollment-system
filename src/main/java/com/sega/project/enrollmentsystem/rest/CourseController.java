package com.sega.project.enrollmentsystem.rest;

import com.sega.project.enrollmentsystem.dto.CourseDTO;
import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.jdbc.CourseJdbcDAO;
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
public class CourseController {

    @Autowired
    CourseJdbcDAO courseRepo;
    @Autowired()
    ModelMapper modelMapper;

    @GetMapping("/courses")
    public List<Course> getStudents() {
        return courseRepo.findAll();
    }

    @GetMapping("/courses/{courseId}")
    public Course getCourseById(@PathVariable int courseId) {

        System.out.println("Result: " + courseRepo.findById(courseId));
        return courseRepo.findById(courseId);
    }

    @GetMapping("/courses/name/{courseName}")
    public List<Course> getCourseByName(@PathVariable String courseName) {
        return courseRepo.findByName(courseName);
    }

    @GetMapping("/courses/semester/{semester}")
    public List<Course> getCourseBySemester(@PathVariable String semester) {
        return courseRepo.findBySemester(semester);
    }

    @PostMapping("/courses")
    public String addCourse(@RequestBody @Valid Course course) {
        int newID = courseRepo.insertCourse(course);

        return "Course #" + newID + " added: " + course.getCourseName();
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<String> updateCourse(@Valid @RequestBody CourseDTO courseDTO, @PathVariable @NotBlank int id) {
        Course course = modelMapper.map(courseDTO, Course.class);
        course.setCourseId(id);
        if (courseRepo.findById(id) == null) {
            throw new EntityNotFoundException("Student id not found - " + course.getCourseId());
        } else {
            courseRepo.updateCourse(course);
            return new ResponseEntity<>("Course #" + id + " updated", HttpStatus.OK);
        }
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
        if (courseRepo.findById(id) == null) {
            throw new EntityNotFoundException("Course id not found - " + id);
        } else {
            courseRepo.deleteById(id);
            return new ResponseEntity<>("Course #" + id + " deleted", HttpStatus.OK);
        }
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
    public ResponseEntity<CustomErrorResponse> handleException(BindException exc) {

        CustomErrorResponse error = new CustomErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
