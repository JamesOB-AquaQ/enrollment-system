package com.sega.project.enrollmentsystem;

import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.jdbc.EnrolmentJdbcDAO;
import com.sega.project.enrollmentsystem.rest.exceptions.EnrolmentException;
import com.sega.project.enrollmentsystem.rest.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EnrollmentSystemApplication.class)
public class EnrolmentJdbcDAOTests {

    @Mock
    JdbcTemplate jdbcTemplate;

    @Spy
    @InjectMocks
    private EnrolmentJdbcDAO enrolmentJdbcDAO;

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
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt())).thenReturn(1);
        doReturn(10).when(enrolmentJdbcDAO).checkRemainingCourseCapacity(anyInt());
        doReturn(false).when(enrolmentJdbcDAO).checkIfStudentEnrolled(anyInt(), anyInt());
        int result = enrolmentJdbcDAO.enrollStudentInCourse(5, 5);
        Assert.isTrue(result==1, "Student is not enrolled");
    }
    @Test
    public void TestEnrollStudentInCourse_MaxCapacity() {
        doReturn(false).when(enrolmentJdbcDAO).checkIfStudentEnrolled(anyInt(), anyInt());
        doReturn(0).when(enrolmentJdbcDAO).checkRemainingCourseCapacity(anyInt());

        Exception exception = assertThrows(EnrolmentException.class, () -> enrolmentJdbcDAO.enrollStudentInCourse(5, 5));

        assertEquals("Course is full", exception.getMessage());
    }
    @Test
    public void TestEnrollStudentInCourse_InsufficientCredit() {
        doReturn(false).when(enrolmentJdbcDAO).checkIfStudentEnrolled(anyInt(), anyInt());
        doReturn(5).when(enrolmentJdbcDAO).checkRemainingCourseCapacity(anyInt());
        doReturn(0).when(enrolmentJdbcDAO).findStudentCreditsRemainingBySemester(anyInt(), anyString());
        doReturn("SPRING2022").when(enrolmentJdbcDAO).findSemesterByCourseId(anyInt());

        Exception exception = assertThrows(EnrolmentException.class, () -> enrolmentJdbcDAO.enrollStudentInCourse(5, 5));
        assertEquals("Insufficient Credits", exception.getMessage());
    }
    @Test
    public void TestEnrollStudentInCourse_StudentAlreadyEnrolled() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt())).thenReturn(1);
        when(jdbcTemplate.query(eq("SELECT * FROM COURSE WHERE course_id=?"), any(RowMapper.class), anyInt())).thenReturn(courses);
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);
        when(jdbcTemplate.queryForObject(eq("SELECT COUNT(*) FROM StudentCourse WHERE student_id=? AND course_id=?"), eq(Integer.class), anyInt(), anyInt())).thenReturn(1);


        Exception exception = assertThrows(EnrolmentException.class, () -> enrolmentJdbcDAO.enrollStudentInCourse(5, 5));

        assertEquals("Student is already enrolled in this course", exception.getMessage());
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
    public void TestCheckIfStudentIsEnrolledInCourse_True() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(eq("SELECT * FROM COURSE WHERE course_id=?"), any(RowMapper.class), anyInt())).thenReturn(courses);
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);
        when(jdbcTemplate.queryForObject(eq("SELECT COUNT(*) FROM StudentCourse WHERE student_id=? AND course_id=?"), eq(Integer.class), anyInt(), anyInt())).thenReturn(1);

        boolean result = enrolmentJdbcDAO.checkIfStudentEnrolled(1, 1);
        Assert.isTrue(result == true, "Student is not enrolled in course");
    }
    @Test
    public void TestCheckIfStudentIsEnrolledInCourse_False() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(eq("SELECT * FROM COURSE WHERE course_id=?"), any(RowMapper.class), anyInt())).thenReturn(courses);
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);
        when(jdbcTemplate.queryForObject(eq("SELECT COUNT(*) FROM StudentCourse WHERE student_id=? AND course_id=?"), eq(Integer.class), anyInt(), anyInt())).thenReturn(0);

        boolean result = enrolmentJdbcDAO.checkIfStudentEnrolled(1, 1);
        Assert.isTrue(result == false, "Student is not enrolled in course");
    }@Test
    public void TestCheckIfStudentIsEnrolledInCourse_FalseNull() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(eq("SELECT * FROM COURSE WHERE course_id=?"), any(RowMapper.class), anyInt())).thenReturn(courses);
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);
        when(jdbcTemplate.queryForObject(eq("SELECT COUNT(*) FROM StudentCourse WHERE student_id=? AND course_id=?"), eq(Integer.class), anyInt(), anyInt())).thenReturn(null);

        boolean result = enrolmentJdbcDAO.checkIfStudentEnrolled(1, 1);
        Assert.isTrue(result == false, "Student is not enrolled in course");
    }
    @Test
    public void TestFindStudentCapacity() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt())).thenReturn(5);

        int result = enrolmentJdbcDAO.findStudentCapacity(1);
        Assert.isTrue(result == 5, "Number of student capacity is not correct");
    }
    @Test
    public void TestCheckCourseExists_False() {
        List<Course> courses = new ArrayList<>();
        when(jdbcTemplate.query(eq("SELECT * FROM COURSE WHERE course_id=?"), any(RowMapper.class), anyInt())).thenReturn(courses);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> enrolmentJdbcDAO.checkCourseExists(1));

        assertEquals("Course with ID: 1 not present in database", exception.getMessage());
    }
    @Test
    public void TestFindStudentCapacity_NoCapacity() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt())).thenReturn(null);

        int result = enrolmentJdbcDAO.findStudentCapacity(1);
        Assert.isTrue(result == 0, "Number of student capacity is not correct");
    }


    @Test
    public void TestFindCoursesByStudentId() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt())).thenReturn(courses);

        List<Course> result = enrolmentJdbcDAO.findCoursesByStudentId(1);
        Assert.isTrue(result.size() == 1, "Number of courses is not correct");
    }


    @Test
    public void TestFindCoursesByStudentIdAndSemester_StudentExistsAndEnrolledInSemester() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt(), anyString())).thenReturn(courses);

        List<Course> result = enrolmentJdbcDAO.findCoursesByStudentIdAndSemester(1, "SPRING2022");

        Assert.isTrue(result.size() == 1, "Number of courses is not correct");
    }
    @Test
    public void TestFindCoursesByStudentIdAndSemester_StudentDoesNotExist() throws Exception {

        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt(), anyString())).thenReturn(courses);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> enrolmentJdbcDAO.findCoursesByStudentIdAndSemester(1, "SPRING2022"));
        assertEquals("Student with ID: 1 not present in database", exception.getMessage());
    }
    @Test
    public void TestRemoveStudentFromCourse() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(eq("SELECT * FROM COURSE WHERE course_id=?"), any(RowMapper.class), anyInt())).thenReturn(courses);
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);
        when(jdbcTemplate.queryForObject(eq("SELECT COUNT(*) FROM StudentCourse WHERE student_id=? AND course_id=?"), eq(Integer.class), anyInt(), anyInt())).thenReturn(1);

        enrolmentJdbcDAO.removeStudentFromCourse(1, 1);
        verify(jdbcTemplate, times(1)).update(anyString(), anyInt(), anyInt());
    }
    @Test
    public void TestRemoveStudentFromCourse_StudentNotEnrolled() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(5, "Physics", "Science", "SPRING2022", 5,50));
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(eq("SELECT * FROM COURSE WHERE course_id=?"), any(RowMapper.class), anyInt())).thenReturn(courses);
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);
        when(jdbcTemplate.queryForObject(eq("SELECT COUNT(*) FROM StudentCourse WHERE student_id=? AND course_id=?"), eq(Integer.class), anyInt(), anyInt())).thenReturn(0);

        Exception exception = assertThrows(EnrolmentException.class, () -> enrolmentJdbcDAO.removeStudentFromCourse(1, 1));
        assertEquals("Student is not enrolled in this course", exception.getMessage());
    }
    @Test
    public void TestFindCoursesByStudentId_NoCourses() {
        List<Course> courses = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(eq("SELECT * FROM STUDENT WHERE student_id=?"), any(RowMapper.class), anyInt())).thenReturn(students);

        when(jdbcTemplate.query(eq("SELECT * FROM Course WHERE course_id IN (SELECT course_id FROM StudentCourse WHERE student_id=?)"), any(RowMapper.class), anyInt())).thenReturn(courses);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> enrolmentJdbcDAO.findCoursesByStudentId(1));
        assertEquals("Student with ID: 1 is not enrolled in any courses", exception.getMessage());
    }




}
