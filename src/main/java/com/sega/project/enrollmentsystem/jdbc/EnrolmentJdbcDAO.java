package com.sega.project.enrollmentsystem.jdbc;

import com.sega.project.enrollmentsystem.rest.InsufficientCreditsException;
import com.sega.project.enrollmentsystem.rest.MaxCapacityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EnrolmentJdbcDAO {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    public GeneratedKeyHolderFactory keyHolderFactory;


    public int findStudentCreditsUsedBySemester(int studentId, String semester) {
        if(jdbcTemplate.queryForObject("SELECT SUM(credit_amount) FROM COURSE WHERE course_id IN (SELECT course_id FROM StudentCourse WHERE student_id=?) AND semester=?", Integer.class, studentId, semester) ==null){
            return 0;
        }
        else{
            return jdbcTemplate.queryForObject("SELECT SUM(credit_amount) FROM COURSE WHERE course_id IN (SELECT course_id FROM StudentCourse WHERE student_id=?) AND semester=?", Integer.class, studentId, semester);
        }
    }
    public int findStudentCreditsRemainingBySemester(int studentId, String semester) {
        return 20-findStudentCreditsUsedBySemester(studentId, semester);
    }
    public int findNumberOfStudentsEnrolled(int courseId) {
        if(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM StudentCourse WHERE course_id=?", Integer.class, courseId) ==null){
            return 0;
        }
        else{
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM StudentCourse WHERE course_id=?", Integer.class, courseId);
        }


    }
    public int findStudentCapacity(int courseId) {
       if(jdbcTemplate.queryForObject("SELECT student_capacity FROM Course WHERE course_id=?", Integer.class, courseId) ==null){
            return 0;
        }
        else{
            return jdbcTemplate.queryForObject("SELECT student_capacity FROM Course WHERE course_id=?", Integer.class, courseId);
        }
    }
    public int findStudentCapacityRemaining(int courseId) {
        return findStudentCapacity(courseId)-findNumberOfStudentsEnrolled(courseId);
    }
    public String findSemesterByCourseId(int courseId) {
        return jdbcTemplate.queryForObject("SELECT semester FROM Course WHERE course_id=?", String.class, courseId);
    }
    public boolean checkIfStudentIsEnrolled(int studentId, int courseId) {
        if(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM StudentCourse WHERE student_id=? AND course_id=?", Integer.class, studentId, courseId) ==null){
            return false;
        }
        else{
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM StudentCourse WHERE student_id=? AND course_id=?", Integer.class, studentId, courseId)>0;
        }
    }

    public int enrollStudentInCourse(int studentId, int courseId) {
        if(checkIfStudentIsEnrolled(studentId,courseId)){
            if (findStudentCapacityRemaining(courseId) > 0) {
                if (findStudentCreditsRemainingBySemester(studentId, findSemesterByCourseId(courseId)) > 0) {
                    return jdbcTemplate.update("INSERT INTO StudentCourse (student_id, course_id) VALUES (?,?)", studentId, courseId);
                } else {
                    throw new InsufficientCreditsException("Insufficient Credits");
                }
            } else {
                throw new MaxCapacityException("Course is full");
            }
        }else{
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

    }
}
