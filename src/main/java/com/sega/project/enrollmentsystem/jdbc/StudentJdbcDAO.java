package com.sega.project.enrollmentsystem.jdbc;

import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.rest.exceptions.EntityNotFoundException;
import com.sega.project.enrollmentsystem.rest.exceptions.InvalidFieldException;
import com.sega.project.enrollmentsystem.rest.exceptions.MissingFieldException;
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

    public List<Student> findById(int id) {
        List<Student> students = jdbcTemplate.query("SELECT * FROM STUDENT WHERE student_id=?",
                new BeanPropertyRowMapper<>(Student.class), id);
        if (!students.isEmpty()) {
            return students;
        } else {
            throw new EntityNotFoundException("No student found with ID: "+id);
        }
    }

    public List<Student> findByName(String forename, String surname) {
        validateStudentName(forename, surname);
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
        validateStudent(student);
        jdbcTemplate.update(c -> {
            PreparedStatement ps = c.
                    prepareStatement("INSERT INTO STUDENT (forename, surname, enrolment_year, graduation_year)" + "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getForename());
            ps.setString(2, student.getSurname());
            ps.setInt(3, Integer.parseInt(student.getEnrolmentYear()));
            ps.setInt(4, Integer.parseInt(student.getGraduationYear()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();

    }

    public int update(Student student) {
        checkStudentExists(student.getStudentId());
        validateStudent(student);
        return jdbcTemplate.update("UPDATE STUDENT SET forename=?, surname=?, enrolment_year=?, graduation_year=? WHERE student_id=?",
                student.getForename(), student.getSurname(), student.getEnrolmentYear(), student.getGraduationYear(), student.getStudentId());
    }
    public void validateStudent(Student student) {
        if(student.getForename() == null || student.getSurname() == null || student.getEnrolmentYear() == null || student.getGraduationYear() == null) {
            throw new MissingFieldException("One or more fields are missing, please provide all fields");
        }
        validateStudentName(student.getForename(),student.getSurname());
        if(student.getEnrolmentYear().length() != 4 || student.getGraduationYear().length() != 4) {
            throw new InvalidFieldException("Enrolment year or graduation year is not valid, please provide valid year");
        }
    }

    private void validateStudentName(String forename,String surname) {
        if(forename == null || surname == null || forename.isEmpty() || surname.isEmpty() || forename.equals("undefined") || surname.equals("undefined")) {
            throw new MissingFieldException("Missing data, provide valid forename and surname");
        }
        if(!forename.trim().matches("[a-zA-Z]+") || !surname.trim().matches("[a-zA-Z]+")) {
            throw new InvalidFieldException("Invalid data, provide valid forename and surname");
        }
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

    public List<Student> findBySemester(String semester) {
        List<Student> students = jdbcTemplate.query("SELECT DISTINCT Student.* FROM Student " +
                        "INNER JOIN StudentCourse " +
                        "ON Student.student_id = StudentCourse.student_id " +
                        "INNER JOIN Course " +
                        "ON StudentCourse.course_id = Course.course_id " +
                        "WHERE Course.semester = ?",
                new BeanPropertyRowMapper<>(Student.class), semester);
        if(!students.isEmpty()) {
            return students;
        }else{
            throw new EntityNotFoundException("No students enrolled in semester: "+semester);
        }
    }





}
