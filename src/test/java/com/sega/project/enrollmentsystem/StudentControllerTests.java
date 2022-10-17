package com.sega.project.enrollmentsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sega.project.enrollmentsystem.dto.StudentDTO;
import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.jdbc.StudentJdbcDAO;
import com.sega.project.enrollmentsystem.rest.StudentController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
//@WebMvcTest
public class StudentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentJdbcDAO studentJdbcDAO;


    private  static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindStudentById() throws Exception{
        Student student = new Student(5,"John","Smith","2021","2025");
        Mockito.when(studentJdbcDAO.findById(anyInt())).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/5")).andExpect(status().isOk()).andExpect(jsonPath("$.forename", Matchers.equalTo("John")));
    }
    @Test
    public void testFindAllStudents() throws Exception{
        List<Student> students = new ArrayList<>();
        students.add(new Student(5,"John","Smith","2021","2025"));
        Mockito.when(studentJdbcDAO.findAll()).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/students")).andExpect(status().isOk()).andExpect(jsonPath("$[0].forename", Matchers.equalTo("John")));
    }

    @Test
    public void testFindStudentByNames() throws Exception{
        List<Student> students = new ArrayList<>();
        students.add(new Student(5,"John","Smith","2021","2025"));
        Mockito.when(studentJdbcDAO.findByName(anyString(),anyString())).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/students?forename=John&surname=Smith")).andExpect(status().isOk()).andExpect(jsonPath("$[0].forename", Matchers.equalTo("John")));

    }

    @Test
    public void testInsertStudent() throws Exception{
        Student student = new Student(5,"John","Smith","2021","2025");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String json =ow.writeValueAsString(student);
        Mockito.when(studentJdbcDAO.insert(any(Student.class))).thenReturn(5);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/students/").contentType("application/json").content(json)).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.equalTo("Student : John Smith enrolled with id: 5")));
    }

    @Test
    public void testUpdateStudent() throws Exception{
        Student student = new Student(1,"John","Smith","2021","2025");
        StudentDTO studentDTO = new StudentDTO("John","Smith","2021","2025");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(studentDTO);

        Mockito.when(studentJdbcDAO.update(any(Student.class))).thenReturn(1);
        Mockito.when(studentJdbcDAO.findById(anyInt())).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/students/1").contentType("application/json").content(json)).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.equalTo("Updated Student #1: John Smith")));
    }

    @Test
    public void testDeleteStudent() throws Exception{
        Student student = new Student(1,"John","Smith","2021","2025");
        Mockito.when(studentJdbcDAO.findById(anyInt())).thenReturn(student);
        Mockito.when(studentJdbcDAO.deleteById(anyInt())).thenReturn(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/1")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.equalTo("Deleted Student #1")));
    }


    @Test
    public void testGetStudentsBySemester() throws Exception{
        List<Student> students = new ArrayList<>();
        students.add(new Student(5,"John","Smith","2021","2025"));
        Mockito.when(studentJdbcDAO.findBySemester(anyString())).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/students?semester=SPRING2021")).andExpect(status().isOk()).andExpect(jsonPath("$[0].forename", Matchers.equalTo("John")));
    }


}
