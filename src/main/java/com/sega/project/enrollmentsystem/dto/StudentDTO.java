package com.sega.project.enrollmentsystem.dto;

import javax.validation.constraints.NotBlank;

public class StudentDTO {
    @NotBlank
    private String forename;
    @NotBlank
    private String surname;
    @NotBlank
    private String enrolmentYear;
    @NotBlank
    private String graduationYear;

    public StudentDTO() {
    }
    public StudentDTO( String forename, String surname, String enrolmentYear, String graduationYear) {
        this.forename = forename;
        this.surname = surname;
        this.enrolmentYear = enrolmentYear;
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

    public String getEnrolmentYear() {
        return enrolmentYear;
    }

    public void setEnrolmentYear(String enrolmentYear) {
        this.enrolmentYear = enrolmentYear;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }


}
