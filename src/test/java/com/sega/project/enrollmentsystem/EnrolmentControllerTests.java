package com.sega.project.enrollmentsystem;


import com.sega.project.enrollmentsystem.jdbc.EnrolmentJdbcDAO;
import com.sega.project.enrollmentsystem.rest.EnrolmentController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    public void enrollCourseTest_ShouldEnrollSuccessfully() throws Exception{
        Mockito.when(enrolmentJdbcDAO.enrollStudentInCourse(anyInt(),anyInt())).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students/1/enroll/1")).andExpect(status().isOk()).andExpect(jsonPath("$").value("Student with id: 1 was enrolled in course with id: 1"));
    }

    @Test
    public void enrollCourseTest_ShouldReturnFailedMessage() throws Exception{
        Mockito.when(enrolmentJdbcDAO.enrollStudentInCourse(anyInt(),anyInt())).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students/1/enroll/1")).andExpect(status().isBadRequest()).andExpect(jsonPath("$").value("Student with id: 1 was not enrolled in course with id: 1"));
    }


}
