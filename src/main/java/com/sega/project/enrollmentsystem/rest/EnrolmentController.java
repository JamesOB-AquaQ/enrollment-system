package com.sega.project.enrollmentsystem.rest;

import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.jdbc.EnrolmentJdbcDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EnrolmentController {

    @Autowired
    EnrolmentJdbcDAO enrolmentRepo;

    public  ResponseEntity<String> createResponse(String body, HttpStatus status) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");

        return ResponseEntity.status(status).headers(responseHeaders).body(body);
    }
    @PostMapping("students/{studentId}/enroll/{courseId}")
    public ResponseEntity<String> enrollCourse(@PathVariable int studentId, @PathVariable int courseId) {
        enrolmentRepo.enrollStudentInCourse(studentId, courseId);
        String body = "{\"StudentID\": " + studentId + ", " +"\"CourseID\": "+ courseId + "}";
        return createResponse(body, HttpStatus.OK);

    }

    @DeleteMapping("students/{studentId}/unenroll/{courseId}")
    public ResponseEntity<String> unenrollCourse(@PathVariable int studentId, @PathVariable int courseId) {
        enrolmentRepo.removeStudentFromCourse(studentId, courseId);
        String body = "Student with id: " + studentId + " was unenrolled from course with id: " + courseId;
        return new ResponseEntity<>(body, HttpStatus.OK);

    }

    @GetMapping("students/{studentId}/courses")
    public List<Course> getStudentCourses(@PathVariable int studentId) {
        return enrolmentRepo.findCoursesByStudentId(studentId);
    }

    @GetMapping(path="students/{studentId}/courses", params={"semester"})
    public List<Course> getStudentCoursesBySemester(@PathVariable int studentId, @RequestParam String semester) {
        return enrolmentRepo.findCoursesByStudentIdAndSemester(studentId, semester);
    }



}
