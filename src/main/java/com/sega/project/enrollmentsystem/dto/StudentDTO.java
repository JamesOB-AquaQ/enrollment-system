package com.sega.project.enrollmentsystem.dto;

import javax.validation.constraints.NotBlank;

public class StudentDTO {
    @NotBlank
    private String forename;
    @NotBlank
    private String surname;
    @NotBlank
    private String enrollmentYear;
    @NotBlank
    private String graduationYear;

    public StudentDTO() {
    }
    public StudentDTO( String forename, String surname, String enrollmentYear, String graduationYear) {
        this.forename = forename;
        this.surname = surname;
        this.enrollmentYear = enrollmentYear;
        this.graduationYear = graduationYear;
    }





    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(String enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }


}
