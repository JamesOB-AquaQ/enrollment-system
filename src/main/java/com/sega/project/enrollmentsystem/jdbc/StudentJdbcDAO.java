package com.sega.project.enrollmentsystem.jdbc;

import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.rest.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class StudentJdbcDAO {

    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Autowired
    public GeneratedKeyHolderFactory keyHolderFactory;

    public List<Student> findAll() {
        List<Student> students = jdbcTemplate.query("SELECT * FROM STUDENT",
                new BeanPropertyRowMapper<>(Student.class));
        if(students.isEmpty()) {
            throw new EntityNotFoundException("No students found");
        }else{
            return students;
        }
    }

    public Student findById(int id) {
        List<Student> students = jdbcTemplate.query("SELECT * FROM STUDENT WHERE student_id=?",
                new BeanPropertyRowMapper<>(Student.class), id);
        if (!students.isEmpty()) {
            return students.get(0);
        } else {
            throw new EntityNotFoundException("Student id not present in database");
        }
    }

    public List<Student> findByName(String forename, String surname) {
        List<Student> students = jdbcTemplate.query("SELECT * FROM STUDENT WHERE forename=? AND surname=?",
                new BeanPropertyRowMapper<>(Student.class), forename, surname);
        if(!students.isEmpty()) {
            return students;
        }else{
            throw new EntityNotFoundException("Student with name: "+forename+" "+surname+" not present in database");
        }
    }

    public int insert(Student student) {
        KeyHolder keyHolder = keyHolderFactory.newGeneratedKeyHolder();

        jdbcTemplate.update(c -> {
            PreparedStatement ps = c.
                    prepareStatement("INSERT INTO STUDENT (forename, surname, enrollment_year, graduation_year)" + "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getForename());
            ps.setString(2, student.getSurname());
            ps.setInt(3, Integer.parseInt(student.getEnrollmentYear()));
            ps.setInt(4, Integer.parseInt(student.getGraduationYear()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();

    }

    public int update(Student student) {
        checkStudentExists(student.getStudentId());
        return jdbcTemplate.update("UPDATE STUDENT SET forename=?, surname=?, enrollment_year=?, graduation_year=? WHERE student_id=?",
                student.getForename(), student.getSurname(), student.getEnrollmentYear(), student.getGraduationYear(), student.getStudentId());
    }

    public int deleteById(int id) {
        checkStudentExists(id);
        return jdbcTemplate.update("DELETE FROM STUDENT WHERE student_id=?", id);
    }


    public void checkStudentExists(int id) {
        List<Student> students = jdbcTemplate.query("SELECT * FROM STUDENT WHERE student_id=?",
                new BeanPropertyRowMapper<>(Student.class), id);
        if (students.isEmpty())
            throw new EntityNotFoundException("Student with id: "+id+" not present in database");
    }



}
