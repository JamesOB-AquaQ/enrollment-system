package com.sega.project.enrollmentsystem;

import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.jdbc.CourseJdbcDAO;
import com.sega.project.enrollmentsystem.jdbc.GeneratedKeyHolderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes =EnrollmentSystemApplication.class)
public class CourseJdbcDAOTests {

    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CourseJdbcDAO courseJdbcDAO;

    @Mock
    private GeneratedKeyHolderFactory keyHolderFactory;

    // Test findAllCourses method
    @Test
    public void TestFindAllCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(courses);
        List<Course> results = courseJdbcDAO.findAll();
        Assert.isTrue(results.size() == 1, "Should be 1 course");

    }

    // Test findById method
    @Test
    public void TestFindCourseById() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt())).thenReturn(courses);
        Course result = courseJdbcDAO.findById(5);
        Assert.notNull(result, "Course not found");



    }

    // Test findByNames method
    @Test
    public void TestFindCourseByName() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5, 50));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString())).thenReturn(courses);
        List<Course> result = courseJdbcDAO.findByName("Physics");
        Assert.notNull(result, "Course not found");
    }

    // Test findBySemester method
    @Test
    public void TestFindCourseBySemester() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5, 50));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString())).thenReturn(courses);
        List<Course> result = courseJdbcDAO.findBySemester("SPRING2022");
        Assert.notNull(result, "Course not found");
    }

    // Test insertCourse method
    @Test
    public void TestInsertCourse() {
        Course course = new Course(5, "Physics", "Science", "SPRING2022", 5, 50);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder(Arrays.asList(Map.of("CourseId", 1)));
        when(keyHolderFactory.newGeneratedKeyHolder()).thenReturn(keyHolder);
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), any(KeyHolder.class))).thenReturn(1);
        int result = courseJdbcDAO.insertCourse(course);
        Assert.isTrue(result == 1, "Course not inserted");
    }

    // Test updateCourse method
    @Test
    public void TestUpdateCourse() {
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyInt())).thenReturn(1);
        int result = courseJdbcDAO.updateCourse(new Course(5, "Physics", "Science", "SPRING2022", 5, 50));
        Assert.isTrue(result == 1, "Course not updated");
    }

}
