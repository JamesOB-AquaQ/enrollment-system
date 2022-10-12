package com.sega.project.enrollmentsystem.entity;

public class Course {
    private int courseId;
    private String courseName;
    private String subjectArea;
    private String semester;
    private int creditAmount;
    private int studentCapacity;

    public Course() {
    }

    public Course(int courseId, String courseName, String subjectArea, String semester, int creditAmount, int studentCapacity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.subjectArea = subjectArea;
        this.semester = semester;
        this.creditAmount = creditAmount;
        this.studentCapacity = studentCapacity;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSubjectArea() {
        return subjectArea;
    }

    public void setSubjectArea(String subjectArea) {
        this.subjectArea = subjectArea;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }

    public int getStudentCapacity() {
        return studentCapacity;
    }

    public void setStudentCapacity(int studentCapacity) {
        this.studentCapacity = studentCapacity;
    }

}
