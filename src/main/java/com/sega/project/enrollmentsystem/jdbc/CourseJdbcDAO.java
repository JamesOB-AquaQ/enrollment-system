package com.sega.project.enrollmentsystem.jdbc;

import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.rest.EntityNotFoundException;
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

    public Course findById(int id) {
        List<Course> courses = jdbcTemplate.query("SELECT * FROM COURSE WHERE course_id=?",
                new BeanPropertyRowMapper<>(Course.class), id);
        if (!courses.isEmpty()) {
            return courses.get(0);
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
            throw new EntityNotFoundException("Course id not present in database");
        }
    }

    public List<Course> findBySemester(String semester) {
        List<Course> courses = jdbcTemplate.query("SELECT * FROM COURSE WHERE semester=?",
                new BeanPropertyRowMapper<>(Course.class), semester);
        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new EntityNotFoundException("Course id not present in database");
        }
    }
    public List<Course> findBySubjectArea(String subjectArea) {
        List<Course> courses = jdbcTemplate.query("SELECT * FROM COURSE WHERE subject_area=?",
                new BeanPropertyRowMapper<>(Course.class), subjectArea);
        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new EntityNotFoundException("Course id not present in database");
        }
    }
    public int insertCourse(Course course) {
        KeyHolder keyHolder = keyHolderFactory.newGeneratedKeyHolder();

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

    public int updateCourse(@NotNull Course course) {
        checkCourseExists(course.getCourseId());
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
