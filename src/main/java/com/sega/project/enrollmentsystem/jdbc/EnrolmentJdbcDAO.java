package com.sega.project.enrollmentsystem.jdbc;

import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.rest.EntityNotFoundException;
import com.sega.project.enrollmentsystem.rest.InsufficientCreditsException;
import com.sega.project.enrollmentsystem.rest.MaxCapacityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrolmentJdbcDAO {

    @Autowired
    public JdbcTemplate jdbcTemplate;


    public int findStudentCreditsUsedBySemester(int studentId, String semester) {
        if (jdbcTemplate.queryForObject("SELECT SUM(credit_amount) FROM COURSE WHERE course_id IN (SELECT course_id FROM StudentCourse WHERE student_id=?) AND semester=?", Integer.class, studentId, semester) == null) {
            return 0;
        } else {
            return jdbcTemplate.queryForObject("SELECT SUM(credit_amount) FROM COURSE WHERE course_id IN (SELECT course_id FROM StudentCourse WHERE student_id=?) AND semester=?", Integer.class, studentId, semester);
        }
    }

    public int findStudentCreditsRemainingBySemester(int studentId, String semester) {
        return 20 - findStudentCreditsUsedBySemester(studentId, semester);
    }

    public int findNumberOfStudentsEnrolled(int courseId) {
        if (jdbcTemplate.queryForObject("SELECT COUNT(*) FROM StudentCourse WHERE course_id=?", Integer.class, courseId) == null) {
            return 0;
        } else {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM StudentCourse WHERE course_id=?", Integer.class, courseId);
        }


    }

    public int findStudentCapacity(int courseId) {
        if (jdbcTemplate.queryForObject("SELECT student_capacity FROM Course WHERE course_id=?", Integer.class, courseId) == null) {
            return 0;
        } else {
            return jdbcTemplate.queryForObject("SELECT student_capacity FROM Course WHERE course_id=?", Integer.class, courseId);
        }
    }

    public int checkRemainingCourseCapacity(int courseId) {
        return findStudentCapacity(courseId) - findNumberOfStudentsEnrolled(courseId);
    }

    public String findSemesterByCourseId(int courseId) {
        return jdbcTemplate.queryForObject("SELECT semester FROM Course WHERE course_id=?", String.class, courseId);
    }

    public boolean checkIfStudentEnrolled(int studentId, int courseId) {
        checkStudentExist(studentId);
        checkCourseExists(courseId);
        try {
            if (jdbcTemplate.queryForObject("SELECT COUNT(*) FROM StudentCourse WHERE student_id=? AND course_id=?", Integer.class, studentId, courseId) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }


    public void checkStudentExist(int studentId) {
        List<Student> students = jdbcTemplate.query("SELECT * FROM STUDENT WHERE student_id=?",
                new BeanPropertyRowMapper<>(Student.class), studentId);
        if (students.isEmpty())
            throw new EntityNotFoundException("Student with ID: "+studentId+" not present in database");
    }

    public void checkCourseExists(int courseId) {
        List<Course> courses = jdbcTemplate.query("SELECT * FROM COURSE WHERE course_id=?",
                new BeanPropertyRowMapper<>(Course.class), courseId);
        if (courses.isEmpty())
            throw new EntityNotFoundException("Course with ID: "+courseId+" not present in database");
    }

    public int enrollStudentInCourse(int studentId, int courseId) {

        if (!checkIfStudentEnrolled(studentId, courseId)) {
            if (checkRemainingCourseCapacity(courseId) > 0) {
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
    public int removeStudentFromCourse(int studentId, int courseId) {
       if(checkIfStudentEnrolled(studentId, courseId)){
           return jdbcTemplate.update("DELETE FROM StudentCourse WHERE student_id=? AND course_id=?", studentId, courseId);
       }else{
              throw new IllegalArgumentException("Student is not enrolled in this course");
       }
    }

    public List<Course> findCoursesByStudentId(int studentId) {
        checkStudentExist(studentId);
        List<Course> courses = jdbcTemplate.query("SELECT * FROM Course WHERE course_id IN (SELECT course_id FROM StudentCourse WHERE student_id=?)",
                new BeanPropertyRowMapper<>(Course.class), studentId);
        if (courses.isEmpty())
            throw new EntityNotFoundException("Student with ID: "+studentId+" is not enrolled in any courses");
        return courses;
    }

    //find student's courses by semester
    public List<Course> findCoursesByStudentIdAndSemester(int studentId, String semester) {
        checkStudentExist(studentId);
        List<Course> courses = jdbcTemplate.query("SELECT Course.* FROM Course \n" +
                        "INNER JOIN StudentCourse " +
                        "ON Course.course_id = StudentCourse.course_id " +
                        "INNER JOIN Student " +
                        "ON StudentCourse.student_id = Student.student_id " +
                        "WHERE " +
                        "Student.student_id = ? AND Course.semester = ?",
                new BeanPropertyRowMapper<>(Course.class), studentId, semester);
        if (courses.isEmpty())
            throw new EntityNotFoundException("Student with ID: "+studentId+" is not enrolled in any courses for semester: "+semester);
        return courses;
    }
}
