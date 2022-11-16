package com.sega.project.enrollmentsystem;


import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.jdbc.EnrolmentJdbcDAO;
import com.sega.project.enrollmentsystem.rest.EnrolmentController;
import com.sega.project.enrollmentsystem.rest.exceptions.EnrolmentException;
import com.sega.project.enrollmentsystem.rest.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnrolmentController.class)
public class EnrolmentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrolmentJdbcDAO enrolmentJdbcDAO;

    @Test
    public void testEnrollCourse() throws Exception{
        Mockito.when(enrolmentJdbcDAO.enrollStudentInCourse(anyInt(),anyInt())).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students/1/enroll/1")).andExpect(status().isOk()).andExpect(jsonPath("$.StudentID").value(1));
    }

    @Test
    public void testUnenrollCourse() throws Exception{
        Mockito.when(enrolmentJdbcDAO.removeStudentFromCourse(anyInt(),anyInt())).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/1/unenroll/1")).andExpect(status().isOk()).andExpect(jsonPath("$").value("Student with id: 1 was unenrolled from course with id: 1"));
    }

    @Test
    public void testGetStudentCourses() throws Exception{
        Mockito.when(enrolmentJdbcDAO.findCoursesByStudentId(anyInt())).thenReturn(Arrays.asList(new Course(1,"Maths","Maths for beginners","SPRING2021",5,100)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/1/courses")).andExpect(status().isOk()).andExpect(jsonPath("$[0].courseId").value(1));
    }

    @Test
    public void testGetStudentCoursesBySemester_Ok() throws Exception{
        Mockito.when(enrolmentJdbcDAO.findCoursesByStudentIdAndSemester(anyInt(),anyString())).thenReturn(Arrays.asList(new Course(1,"Maths","Maths for beginners","SPRING2021",5,100)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/1/courses?semester=SPRING2021")).andExpect(status().isOk()).andExpect(jsonPath("$[0].courseId").value(1));
    }
    @Test
    public void testGetStudentCoursesBySemester_NotFound() throws Exception{
        Mockito.when(enrolmentJdbcDAO.findCoursesByStudentIdAndSemester(anyInt(),anyString())).thenThrow(new EntityNotFoundException("Student with ID: 1 is not enrolled in any courses for semester: SPRING2021"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/1/courses?semester=SPRING2021")).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Student with ID: 1 is not enrolled in any courses for semester: SPRING2021"));
    }
    @Test
    public void testEnrollCourse_CourseFull() throws Exception{
        Mockito.when(enrolmentJdbcDAO.enrollStudentInCourse(anyInt(),anyInt())).thenThrow(new EnrolmentException("Course is full"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students/1/enroll/1")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Course is full"));
    }




}
