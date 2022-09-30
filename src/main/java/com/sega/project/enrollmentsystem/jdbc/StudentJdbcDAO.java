package com.sega.project.enrollmentsystem.jdbc;

import com.sega.project.enrollmentsystem.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentJdbcDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Student> findAll(){
        return jdbcTemplate.query("SELECT * FROM STUDENT",
                new BeanPropertyRowMapper<>(Student.class));
    }
}
