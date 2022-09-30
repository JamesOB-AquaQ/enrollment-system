package com.sega.project.enrollmentsystem.entity;

public class Student {
    private int studentId;
    private String forename;
    private String surname;
    private int enrollmentYear;
    private int graduationYear;

    public Student() {
    }
    public Student(int studentId, String forename, String surname, int enrollmentYear, int graduationYear) {
        this.studentId = studentId;
        this.forename = forename;
        this.surname = surname;
        this.enrollmentYear = enrollmentYear;
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

    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(int enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    @Override
    public String toString() {
        return "\nStudent{" +
                "studentId=" + studentId +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", enrollmentYear=" + enrollmentYear +
                ", graduationYear=" + graduationYear +
                '}';
    }
}
