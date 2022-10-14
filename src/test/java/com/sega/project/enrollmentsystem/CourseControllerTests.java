package com.sega.project.enrollmentsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sega.project.enrollmentsystem.entity.Course;
import com.sega.project.enrollmentsystem.jdbc.CourseJdbcDAO;
import com.sega.project.enrollmentsystem.rest.CourseController;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
public class CourseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseJdbcDAO courseJdbcDAO;

    private  static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindCourseById() throws Exception{
        Mockito.when(courseJdbcDAO.findById(anyInt())).thenReturn(new Course(1,"Maths","Maths for beginners","SPRING2021",5,100));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/1")).andExpect(status().isOk()).andExpect(jsonPath("$.courseName", Matchers.equalTo("Maths")));
    }

    @Test
    public void testGetCoursesByName() throws Exception{
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1,"Maths","Maths for beginners","SPRING2021",5,100));

        Mockito.when(courseJdbcDAO.findByName("Maths")).thenReturn(courses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses?courseName=Maths")).andExpect(status().isOk()).andExpect(jsonPath("$[0].courseName", Matchers.equalTo("Maths")));
    }

    @Test
    public void testGetCoursesBySemester() throws Exception{
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1,"Maths","Maths for beginners","SPRING2021",5,100));

        Mockito.when(courseJdbcDAO.findBySemester("SPRING2021")).thenReturn(courses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses?semester=SPRING2021")).andExpect(status().isOk()).andExpect(jsonPath("$[0].courseName", Matchers.equalTo("Maths")));
    }

    @Test
    public void testGetCoursesBySubject() throws Exception{
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1,"Maths","Maths for beginners","SPRING2021",5,100));

        Mockito.when(courseJdbcDAO.findBySubjectArea("Maths for beginners")).thenReturn(courses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses?subject="+courses.get(0).getSubjectArea())).andExpect(status().isOk()).andExpect(jsonPath("$[0].courseName", Matchers.equalTo("Maths")));
    }

    @Test
    public void testAddCourse() throws Exception{
        Course course = new Course(1,"Maths","Maths for beginners","SPRING2021",5,100);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String json = mapper.writeValueAsString(course);

        Mockito.when(courseJdbcDAO.insertCourse(any(Course.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses").contentType("application/json").content(json)).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.equalTo("Course #1 added: Maths")));
    }

    @Test
    public void testUpdateCourse() throws Exception{
        Course course = new Course(1,"Maths","Maths for beginners","SPRING2021",5,100);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String json = mapper.writeValueAsString(course);

        Mockito.when(courseJdbcDAO.findById(anyInt())).thenReturn(course);
        Mockito.when(courseJdbcDAO.updateCourse(any(Course.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/1").contentType("application/json").content(json)).andExpect(status().isOk()
        ).andExpect(jsonPath("$", Matchers.equalTo("Course #1 updated")));
    }


    @Test
    public void testDeleteCourse() throws Exception{
        Course course = new Course(1,"Maths","Maths for beginners","SPRING2021",5,100);

        Mockito.when(courseJdbcDAO.findById(anyInt())).thenReturn(course);
        Mockito.when(courseJdbcDAO.deleteCourse(anyInt())).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courses/1")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.equalTo("Course #1 deleted")));
    }








}
