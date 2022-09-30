CREATE TABLE Student
(
    student_id INT NOT NULL AUTO_INCREMENT,
    forename char(50) NOT NULL,
    surname char(50) NOT NULL,
    enrollment_year int NOT NULL,
    graduation_year int NOT NULL,
    PRIMARY KEY(student_id)
);
CREATE TABLE Course
(
    course_id INT NOT NULL AUTO_INCREMENT,
    course_name char(50) NOT NULL,
    subject_area varchar(255) NOT NULL,
    semester varchar(50) NOT NULL,
    credit_amount int NOT NULL,
    student_capacity int NOT NULL,
    PRIMARY KEY(course_id)
);
CREATE TABLE StudentCourse
(
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    CONSTRAINT FK_student_id FOREIGN KEY (student_id) REFERENCES Student(student_id),
    CONSTRAINT FK_course_id FOREIGN KEY (course_id) REFERENCES Course(course_id),
    CONSTRAINT PK_StudentCourse_id PRIMARY KEY (student_id,course_id)
);

INSERT INTO Student (forename,surname,enrollment_year,graduation_year)
VALUES('Jack','Brown',2020,2023);
INSERT INTO Student (forename,surname,enrollment_year,graduation_year)
VALUES('Chad','Darby',2021,2024);
INSERT INTO Student (forename,surname,enrollment_year,graduation_year)
VALUES('Conor','McGee',2022,2025);

INSERT INTO Course (course_name,subject_area,semester,credit_amount,student_capacity)
VALUES('Statistics','Maths','SPRING2022',5,50);
INSERT INTO Course (course_name,subject_area,semester,credit_amount,student_capacity)
VALUES('Astrophysics','Physics','SPRING2022',10,75);
INSERT INTO Course (course_name,subject_area,semester,credit_amount,student_capacity)
VALUES('Java','Computing','SPRING2022',5,100);

INSERT INTO StudentCourse (student_id,course_id)
VALUES(1,1);
INSERT INTO StudentCourse (student_id,course_id)
VALUES(2,1);
INSERT INTO StudentCourse (student_id,course_id)
VALUES(3,2);
INSERT INTO StudentCourse (student_id,course_id)
VALUES(1,3);
INSERT INTO StudentCourse (student_id,course_id)
VALUES(2,2);
