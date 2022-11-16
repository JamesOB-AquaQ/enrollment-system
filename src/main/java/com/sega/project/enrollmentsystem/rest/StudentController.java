package com.sega.project.enrollmentsystem.rest;

import com.sega.project.enrollmentsystem.dto.StudentDTO;
import com.sega.project.enrollmentsystem.entity.Student;
import com.sega.project.enrollmentsystem.jdbc.StudentJdbcDAO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Student> getStudentById(@PathVariable int studentId){
        return studentRepo.findById(studentId);
    }
    @GetMapping(path="/students", params={"forename", "surname"})
    public List<Student> getStudentsByName(@RequestParam String forename, @RequestParam String surname){
        return studentRepo.findByName(forename,surname);
    }
    @PostMapping("/students")
    public String addStudents(@RequestBody @Valid Student student){
            int newID = studentRepo.insert(student);

            return "Student : "+student.getForename()+ " " + student.getSurname()+ " enrolled with id: "+newID;
    }
    @PutMapping("/students/{id}")
    public ResponseEntity<String> updateStudent(@Valid @RequestBody StudentDTO studentDTO, @PathVariable @NotBlank int id){
        Student student = modelMapper.map(studentDTO, Student.class);
        student.setStudentId(id);

        studentRepo.update(student);

        String body = "Updated Student #"+student.getStudentId()+": "+student.getForename()+ " " + student.getSurname();
        return new ResponseEntity<String>(body, HttpStatus.OK);
    }
    @DeleteMapping("/students/{studentId}")
    public String deleteStudent(@PathVariable int studentId){
        studentRepo.deleteById(studentId);
        return "Deleted Student #"+studentId;
    }

    @GetMapping(path="/students", params={"semester"})
    public List<Student> getStudentsBySemester(@RequestParam String semester){
        return studentRepo.findBySemester(semester);
    }


}
