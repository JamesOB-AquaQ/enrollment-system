package com.sega.project.enrollmentsystem;

import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.jdbc.GeneratedKeyHolderFactory;
import com.sega.project.enrollmentsystem.jdbc.StudentJdbcDAO;
import com.sega.project.enrollmentsystem.rest.exceptions.EntityNotFoundException;
import com.sega.project.enrollmentsystem.rest.exceptions.InvalidFieldException;
import com.sega.project.enrollmentsystem.rest.exceptions.MissingFieldException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EnrollmentSystemApplication.class)
public class StudentJdbcDAOTests {

    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    private StudentJdbcDAO studentJdbcDAO;

    @Mock
    private GeneratedKeyHolderFactory keyHolderFactory;

    @Test
    public void testFindAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(students);
        List<Student> results = studentJdbcDAO.findAll();
        Assert.isTrue(results.size() == 1, "Should be 1 student");
    }
    @Test
    public void testFindAllStudents_NoStudentsFound()
    {
        List<Student> students = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(students);

        assertThrows(EntityNotFoundException.class, () -> studentJdbcDAO.findAll());
    }
    @Test
    public void testFindStudentById() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt())).thenReturn(students);

        List<Student> result = studentJdbcDAO.findById(5);

        Assert.notNull(result.get(0), "Student not found");

    }
    @Test
    public void testFindStudentById_NotFound() {
        List<Student> students = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt())).thenReturn(students);

        assertThrows(EntityNotFoundException.class, () -> studentJdbcDAO.findById(1));

    }

    @Test
    public void testFindStudentByName() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString(), anyString())).thenReturn(students);

        List<Student> results = studentJdbcDAO.findByName("John", "Smith");

        Assert.notNull(results, "Student not found");

    }
    @Test
    public void testFindStudentByName_NotFound() {
        List<Student> students = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString(), anyString())).thenReturn(students);

        assertThrows(EntityNotFoundException.class, () -> studentJdbcDAO.findByName("John", "Smith"));
    }
    @Test
    public void testInsertStudent() {
        Student student = new Student(1, "John", "Smith", "2021", "2025");
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder(Arrays.asList(Map.of("StudentId", 1)));
        when(keyHolderFactory.newGeneratedKeyHolder()).thenReturn( keyHolder);
        //when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenReturn(1);
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString(), anyString(), any(KeyHolder.class))).thenReturn(1);

        int result = studentJdbcDAO.insert(student);
        Assert.isTrue(result == 1, "Student not inserted");
    }

    @Test
    public void testGeneratedKeyHolderFactory() {
        GeneratedKeyHolderFactory keyHolderFactory= new GeneratedKeyHolderFactory();
        KeyHolder keyHolder = keyHolderFactory.newGeneratedKeyHolder();
        assertEquals(keyHolderFactory.newGeneratedKeyHolder().getClass(), GeneratedKeyHolder.class, "GeneratedKeyHolderFactory not working");
        Assert.notNull(keyHolder, "GeneratedKeyHolderFactory not working");
    }


    @Test
    public void testUpdateStudent() {
        Student student = new Student(1, "John", "Smith", "2021", "2025");
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt())).thenReturn(1);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyInt())).thenReturn(Arrays.asList(student));
        int result = studentJdbcDAO.update(student);
        Assert.isTrue(result == 1, "Student not updated");
    }
    @Test
    public void testUpdateStudent_invalidName() {
        Student student = new Student(1, "123", "Smith", "2021", "2025");
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt())).thenReturn(1);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyInt())).thenReturn(Arrays.asList(student));

        assertThrows(InvalidFieldException.class, () -> studentJdbcDAO.update(student));
    }

    @Test
    public void testDeleteStudent() {
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyInt())).thenReturn(Arrays.asList(new Student(1, "John", "Smith", "2021", "2025")));

        int result = studentJdbcDAO.deleteById(1);
        Assert.isTrue(result == 1, "Student not deleted");
    }
    @Test
    public void testDeleteStudent_NotFound() {
        List<Student> students = new ArrayList<>();

        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class),anyInt())).thenReturn(students);

        assertThrows(EntityNotFoundException.class, () -> studentJdbcDAO.deleteById(1));
    }


    @Test
    public void testFindStudentBySemester(){
        List<Student> students = new ArrayList<>();
        students.add(new Student(5, "John", "Smith", "2021", "2025"));
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString())).thenReturn(students);
        List<Student> results = studentJdbcDAO.findBySemester("SPRING2021");
        Assert.notNull(results, "Students not found");
        assertEquals(results.size(), 1, "Students not found");
        assertEquals(results.get(0).getForename(), "John", "Students not found");
    }
    @Test
    public void testFindStudentBySemester_NoStudentsEnrolled(){
        List<Student> students = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString())).thenReturn(students);

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
            studentJdbcDAO.findBySemester("SPRING2021"));
        assertEquals("No students enrolled in semester: SPRING2021", exception.getMessage());
    }
    @Test
    public void testValidateStudent_MissingField() {
        Student student = new Student(1, "John", null, "2021", "2021");
        Exception exception = assertThrows(MissingFieldException.class, () -> studentJdbcDAO.validateStudent(student));
        assertEquals("One or more fields are missing, please provide all fields", exception.getMessage());
    }
     @Test
    public void testValidateStudent_InvalidGraduationYear() {
        Student student = new Student(1, "John", "Smith", "2021", "21");
        Exception exception = assertThrows(InvalidFieldException.class, () -> studentJdbcDAO.validateStudent(student));
        assertEquals("Enrolment year or graduation year is not valid, please provide valid year", exception.getMessage());
    }
    @Test
    public void testValidateStudent_EmptyName() {
        Student student = new Student(1, "", "", "2021", "21");
        Exception exception = assertThrows(MissingFieldException.class, () -> studentJdbcDAO.validateStudent(student));
        assertEquals("Missing data, provide valid forename and surname", exception.getMessage());
    }


}
