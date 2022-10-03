package com.sega.project.enrollmentsystem.jdbc;

import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.rest.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;

@Repository
public class StudentJdbcDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Student> findAll(){
        return jdbcTemplate.query("SELECT * FROM STUDENT",
                new BeanPropertyRowMapper<>(Student.class));
    }
    public Student findById(int id){
        List<Student> students = jdbcTemplate.query("SELECT * FROM STUDENT WHERE student_id=?",
                new BeanPropertyRowMapper<>(Student.class), id);
        if(!students.isEmpty()){
            return students.get(0);
        }else{
            throw new StudentNotFoundException("Student id not present in database");
        }
    }
    public List<Student> findByName(String forename, String surname){
        return jdbcTemplate.query("SELECT * FROM STUDENT WHERE forename=? AND surname=?",
                new BeanPropertyRowMapper<>(Student.class), forename,surname);
    }

}
