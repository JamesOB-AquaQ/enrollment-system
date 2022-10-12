package com.sega.project.enrollmentsystem;

import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.jdbc.GeneratedKeyHolderFactory;
import com.sega.project.enrollmentsystem.jdbc.StudentJdbcDAO;
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
public class StudentJdbcDAOTests {

    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    private StudentJdbcDAO studentJdbcDAO;

    @Mock
    private GeneratedKeyHolderFactory keyHolderFactory;

    @Test
    public void TestFindAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(students);
        List<Student> results = studentJdbcDAO.findAll();
        Assert.isTrue(results.size() == 1, "Should be 1 student");
    }

    // Test findById
    @Test
    public void TestFindStudentById() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt())).thenReturn(students);
        Student result = studentJdbcDAO.findById(5);
        Assert.notNull(result, "Student not found");

    }

    // Test findByNames method
    @Test
    public void TestFindStudentByNames() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString(), anyString())).thenReturn(students);
        List<Student> results = studentJdbcDAO.findByName("John", "Smith");
        Assert.notNull(results, "Student not found");

    }
    // Test insert method
    @Test
    public void TestInsertStudent() {
        Student student = new Student(1, "John", "Smith", "2021", "2025");
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder(Arrays.asList(Map.of("StudentId", 1)));
        when(keyHolderFactory.newGeneratedKeyHolder()).thenReturn( keyHolder);
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString(), anyString(), any(KeyHolder.class))).thenReturn(1);
        //when(jdbcTemplate.update(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString())).thenReturn(1);
        int result = studentJdbcDAO.insert(student);
        Assert.isTrue(result == 1, "Student not inserted");
    }

    // Test update method
    @Test
    public void TestUpdateStudent() {
        Student student = new Student(1, "John", "Smith", "2021", "2025");
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt())).thenReturn(1);
        int result = studentJdbcDAO.update(student);
        Assert.isTrue(result == 1, "Student not updated");
    }

    // Test delete method
    @Test
    public void TestDeleteStudent() {
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);
        int result = studentJdbcDAO.deleteById(1);
        Assert.isTrue(result == 1, "Student not deleted");
    }

}
