package com.sega.project.enrollmentsystem;

import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.jdbc.EnrolmentJdbcDAO;
import com.sega.project.enrollmentsystem.jdbc.GeneratedKeyHolderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes =EnrollmentSystemApplication.class)
public class EnrolmentJdbcDAOTests {

    @Mock
    JdbcTemplate jdbcTemplate;

    @Spy
    @InjectMocks
    private EnrolmentJdbcDAO enrolmentJdbcDAO;

    @Mock
    private GeneratedKeyHolderFactory keyHolderFactory;

    @Test
    public void TestCheckIfStudentEnrolled_StudentNotEnrolled() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt(), anyInt())).thenReturn(null);
        when(jdbcTemplate.query(eq("SELECT * FROM COURSE WHERE course_id=?"), any(RowMapper.class), anyInt())).thenReturn(courses);
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);

        boolean result = enrolmentJdbcDAO.checkIfStudentEnrolled(5, 5);

        Assert.isTrue(!result, "Student is already enrolled");

    }
    @Test
    public void TestCheckIfStudentEnrolled_StudentIsEnrolled() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt(), anyInt())).thenReturn(1);
        when(jdbcTemplate.query(eq("SELECT * FROM COURSE WHERE course_id=?"), any(RowMapper.class), anyInt())).thenReturn(courses);
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);
        boolean result = enrolmentJdbcDAO.checkIfStudentEnrolled(5, 5);
        Assert.isTrue(result, "Student is not enrolled");

    }

    @Test
    public void TestFindSemesterByCourseId() {
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), anyInt())).thenReturn("SPRING2022");

        String result = enrolmentJdbcDAO.findSemesterByCourseId(5);
        Assert.isTrue(result.equals("SPRING2022"), "Semester is not correct");

    }
    @Test
    public void TestFindNumberOfStudentsEnrolled_StudentsEnrolled() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt())).thenReturn(5);

        int result = enrolmentJdbcDAO.findNumberOfStudentsEnrolled(1);
        Assert.isTrue(result == 5, "Number of students enrolled is not correct");
    }
    @Test
    public void TestFindNumberOfStudentsEnrolled_NoStudentsEnrolled() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt())).thenReturn(null);

        int result = enrolmentJdbcDAO.findNumberOfStudentsEnrolled(1);
        Assert.isTrue(result == 0, "Number of students enrolled is not correct");
    }

    @Test
    public void TestEnrollStudentInCourse_StudentEnrolled() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt(), anyInt())).thenReturn(null);
        when(jdbcTemplate.query(eq("SELECT * FROM COURSE WHERE course_id=?"), any(RowMapper.class), anyInt())).thenReturn(courses);
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt())).thenReturn(1);
        when(keyHolderFactory.newGeneratedKeyHolder()).thenReturn(new GeneratedKeyHolder());
        when(jdbcTemplate.update(anyString(), any(Map.class), any(KeyHolder.class))).thenReturn(1);
        doReturn(10).when(enrolmentJdbcDAO).checkRemainingCourseCapacity(anyInt());
        int result = enrolmentJdbcDAO.enrollStudentInCourse(5, 5);
        Assert.isTrue(result==1, "Student is not enrolled");
    }

    @Test
    public void TestFindStudentCreditsUsedBySemester() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt(), anyString())).thenReturn(5);

        int result = enrolmentJdbcDAO.findStudentCreditsUsedBySemester(1, "SPRING2022");
        Assert.isTrue(result == 5, "Number of credits used is not correct");
    }

    @Test
    public void TestFindStudentCreditsRemainingBySemester() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt(), anyString())).thenReturn(5);

        int result = enrolmentJdbcDAO.findStudentCreditsRemainingBySemester(1, "SPRING2022");
        Assert.isTrue(result == 15, "Number of credits remaining is not correct");
    }

    @Test
    public void TestCheckRemainingCourseCapacity() {
        when(jdbcTemplate.queryForObject(eq("SELECT COUNT(*) FROM StudentCourse WHERE course_id=?"), eq(Integer.class), anyInt())).thenReturn(5);
        when(jdbcTemplate.queryForObject(eq("SELECT student_capacity FROM Course WHERE course_id=?"), eq(Integer.class), anyInt())).thenReturn(5);

        int result = enrolmentJdbcDAO.checkRemainingCourseCapacity(1);
        Assert.isTrue(result == 0, "Number of remaining course capacity is not correct");
    }

    @Test
    public void TestFindStudentCapacity() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt())).thenReturn(5);

        int result = enrolmentJdbcDAO.findStudentCapacity(1);
        Assert.isTrue(result == 5, "Number of student capacity is not correct");
    }


}
