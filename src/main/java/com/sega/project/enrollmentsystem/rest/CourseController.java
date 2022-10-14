package com.sega.project.enrollmentsystem.rest;

import com.sega.project.enrollmentsystem.dto.CourseDTO;
import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.jdbc.CourseJdbcDAO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Course> getCourses() {
        return courseRepo.findAll();
    }

    @GetMapping("/courses/{courseId}")
    public Course findCourseById(@PathVariable int courseId) {

        System.out.println("Result: " + courseRepo.findById(courseId));
        return courseRepo.findById(courseId);
    }

    @GetMapping(path = "/courses", params={"courseName"})
    public List<Course> getCoursesByName(@RequestParam String courseName) {
        return courseRepo.findByName(courseName);
    }

    @GetMapping(path = "/courses", params = {"semester"})
    public List<Course> getCoursesBySemester(@RequestParam String semester) {
        return courseRepo.findBySemester(semester);
    }

    @GetMapping(path = "/courses", params = {"subject"})
    public List<Course> getCoursesBySubject(@RequestParam String subject) {
        return courseRepo.findBySubjectArea(subject);
    }
//

    @PostMapping("/courses")
    public String addCourse(@RequestBody @Valid Course course) {
        int newID = courseRepo.insertCourse(course);

        return "Course #" + newID + " added: " + course.getCourseName();
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<String> updateCourse(@Valid @RequestBody CourseDTO courseDTO, @PathVariable @NotBlank int id) {
        Course course = modelMapper.map(courseDTO, Course.class);
        course.setCourseId(id);
        courseRepo.updateCourse(course);
        return new ResponseEntity<>("Course #" + id + " updated", HttpStatus.OK);

    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
            courseRepo.deleteCourse(id);
            return new ResponseEntity<>("Course #" + id + " deleted", HttpStatus.OK);

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
    public ResponseEntity<CustomErrorResponse> handleException(Exception exc) {

        CustomErrorResponse error = new CustomErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
