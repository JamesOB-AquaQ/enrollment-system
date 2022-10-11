package com.sega.project.enrollmentsystem.rest;

import com.sega.project.enrollmentsystem.dto.StudentDTO;
import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.jdbc.StudentJdbcDAO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    StudentJdbcDAO studentRepo;
    @Autowired()
    ModelMapper modelMapper;
    @GetMapping("/students")
    public List<Student> getStudents(){
        return studentRepo.findAll();
    }
    @GetMapping("/students/{studentId}")
    public Student getStudentById(@PathVariable int studentId){
        if(studentRepo.findById(studentId)==null){
            throw new StudentNotFoundException("Student id not found - "+studentId);
        }
        System.out.println("Result: "+studentRepo.findById(10));
        return studentRepo.findById(studentId);
    }
    @GetMapping("/students/name")
    public List<Student> getStudentsByName(@RequestParam String forename, @RequestParam String surname){
        return studentRepo.findByName(forename,surname);
    }
    @PostMapping("/students/")
    public String addStudents(@RequestBody @Valid Student student){
            int newID = studentRepo.insert(student);

            return "Student : "+student.getForename()+ " " + student.getSurname()+ " enrolled with id: "+newID;
    }
    @PutMapping("/students/{id}")
    public ResponseEntity<String> updateStudent(@Valid @RequestBody StudentDTO studentDTO, @PathVariable @NotBlank int id){
        Student student = modelMapper.map(studentDTO, Student.class);
        student.setStudentId(id);
        if(studentRepo.findById(id)==null){
            throw new StudentNotFoundException("Student id not found - "+student.getStudentId());
        }
        try {
            studentRepo.update(student);
        }catch (EmptyResultDataAccessException e){
            throw new StudentNotFoundException("Student id not found - "+id);
        }
        String body = "Updated Student #"+student.getStudentId()+": "+student.getForename()+ " " + student.getSurname();
        return new ResponseEntity<String>(body, HttpStatus.OK);
    }
    @DeleteMapping("/students/{studentId}")
    public String deleteStudent(@PathVariable int studentId){
        if(studentRepo.findById(studentId)==null){
            throw new StudentNotFoundException("Student id not found - "+studentId);
        }
        studentRepo.deleteById(studentId);
        return "Deleted Student #"+studentId;
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {

        StudentErrorResponse error = new StudentErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(BindException exc) {

        StudentErrorResponse error = new StudentErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
