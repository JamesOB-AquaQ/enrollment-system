package com.sega.project.enrollmentsystem.entity;

public class Student {


    private int studentId;
    private String forename;
    private String surname;
    private String enrolmentYear;
    private String graduationYear;

    public Student() {
    }
    public Student(int studentId, String forename, String surname, String enrolmentYear, String graduationYear) {
        this.studentId = studentId;
        this.forename = forename;
        this.surname = surname;
        this.enrolmentYear = enrolmentYear;
        this.graduationYear = graduationYear;
    }



    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

    @Override
    public String toString() {
        return "\nStudent{" +
                "studentId=" + studentId +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", enrollmentYear=" + enrolmentYear +
                ", graduationYear=" + graduationYear +
                '}';
    }
}
