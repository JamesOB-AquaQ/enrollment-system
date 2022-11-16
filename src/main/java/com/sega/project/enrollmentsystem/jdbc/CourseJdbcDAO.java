package com.sega.project.enrollmentsystem.jdbc;

import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.rest.exceptions.EntityNotFoundException;
import com.sega.project.enrollmentsystem.rest.exceptions.InvalidFieldException;
import com.sega.project.enrollmentsystem.rest.exceptions.MissingFieldException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CourseJdbcDAO {

    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Autowired
    public GeneratedKeyHolderFactory keyHolderFactory;

    public List<Course> findAll() {
        List<Course> courses = jdbcTemplate.query("SELECT * FROM COURSE",
                new BeanPropertyRowMapper<>(Course.class));
        if(courses.isEmpty()) {
            throw new EntityNotFoundException("No courses found");
        }else{
            return courses;
        }
    }

    public List<Course> findById(int id) {
        List<Course> courses = jdbcTemplate.query("SELECT * FROM COURSE WHERE course_id=?",
                new BeanPropertyRowMapper<>(Course.class), id);
        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new EntityNotFoundException("Course with id: "+id+" not present in database");
        }
    }
    public List<Course> findByName(String courseName) {
        List<Course> courses = jdbcTemplate.query("SELECT * FROM COURSE WHERE course_name=?",
                new BeanPropertyRowMapper<>(Course.class), courseName);
        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new EntityNotFoundException("No courses found with name: "+courseName);
        }
    }

    public List<Course> findBySemester(String semester) {
        validateSemester(semester);
        List<Course> courses = jdbcTemplate.query("SELECT * FROM COURSE WHERE semester=?",
                new BeanPropertyRowMapper<>(Course.class), semester);
        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new EntityNotFoundException("No courses found for semester: "+semester);
        }
    }
    public List<Course> findBySubjectArea(String subjectArea) {
        List<Course> courses = jdbcTemplate.query("SELECT * FROM COURSE WHERE subject_area=?",
                new BeanPropertyRowMapper<>(Course.class), subjectArea);
        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new EntityNotFoundException("No courses with subject area: "+subjectArea);
        }
    }
    public int insertCourse(Course course) {
        KeyHolder keyHolder = keyHolderFactory.newGeneratedKeyHolder();
        validateCourse(course);
        jdbcTemplate.update(c -> {
            PreparedStatement ps = c.
                    prepareStatement("INSERT INTO COURSE (course_name, subject_area, semester, credit_amount, student_capacity)" + "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getSubjectArea());
            ps.setString(3, course.getSemester());
            ps.setInt(4, course.getCreditAmount());
            ps.setInt(5, course.getStudentCapacity());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();

    }

    public  void validateCourse(Course course) {
        if (course.getCourseName() == null || course.getCourseName().isEmpty()) {
            throw new MissingFieldException("Course name cannot be empty");
        }
        if (course.getSubjectArea() == null || course.getSubjectArea().isEmpty()) {
            throw new MissingFieldException("Subject area cannot be empty");
        }
        validateSemester(course.getSemester());
        if (course.getCreditAmount() == 0) {
            throw new MissingFieldException("Credit amount cannot be empty");
        }
        if (course.getStudentCapacity() == 0) {
            throw new MissingFieldException("Student capacity cannot be empty");
        }
    }

    private void validateSemester(String semester) {
        if (semester == null || semester.isEmpty() || !semester.matches("^[A-Z]+[0-9]{4}$")) {
            throw new InvalidFieldException("Semester is invalid, please use format: SEASONYEAR");
        }
    }

    public int updateCourse(@NotNull Course course) {
        checkCourseExists(course.getCourseId());
        validateCourse(course);

        return jdbcTemplate.update("UPDATE COURSE SET course_name=?, subject_area=?, semester=?, credit_amount=?, student_capacity=? WHERE course_id=?",
                course.getCourseName(), course.getSubjectArea(), course.getSemester(), course.getCreditAmount(), course.getStudentCapacity(), course.getCourseId());
    }


    public int deleteCourse(int id) {
        checkCourseExists(id);
        return jdbcTemplate.update("DELETE FROM COURSE WHERE course_id=?", id);
    }
    public void checkCourseExists(int courseId) {
        List<Course> courses = jdbcTemplate.query("SELECT * FROM COURSE WHERE course_id=?",
                new BeanPropertyRowMapper<>(Course.class), courseId);
        if (courses.isEmpty())
            throw new EntityNotFoundException("Course ID not present in database");
    }

}
