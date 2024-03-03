package org.example;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudentTest {

    List<Student> students = new ArrayList<>();
    List<Course> courses = new ArrayList<>();
    Semester sem;

    @BeforeEach
    public void readFromFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/resources/students.csv"));
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] values = line.split(",");
                Faculty fac = new Faculty(values[1]);
                Specialization spec = new Specialization(values[2], fac, Specialization.Type.MAJOR);
                Student stu = new Student(values[0], spec);
                students.add(stu);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/resources/Courses.csv"));
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] values = line.split(",");
                Faculty fac = new Faculty(values[2]);
                int creditHours = Integer.parseInt(values[3]);
                DayOfWeek day = DayOfWeek.valueOf(values[4].toUpperCase());
                Duration duration = Duration.ofMinutes(Long.parseLong(values[5]));
                String room = values[6];
                LocalTime hour = LocalTime.parse(values[7]);
                Course course = new Course(values[0], values[1], fac, creditHours, List
                        .of(new WeeklyMeeting(day, duration, room, hour)), 200);
                courses.add(course);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEnterGradesMethod() {
        sem = new Semester(LocalDate.of(2023, 9, 1), LocalDate.of(2023, 12, 31));
        String[] grades = { "A", "B+", "B", "C+", "C", "D+", "D", "F" };
        ArrayList<String> list = new ArrayList<>(Arrays.asList(grades));
        for (int i = 0; i < courses.size(); i++) {
            if (i != 0) {
                courses.get(i).addPrerequisites(courses.get(i - 1));
            }
            sem.registerInACourse(courses.get(i), students,
                    new Teacher("null",
                            new Specialization("null", new Faculty("null"), Specialization.Type.MAJOR)));
            for (Student stu : students) {
                stu.enterCourseGrade(courses.get(i), list.get(i));
            }
        }
        double numGrades[] = { 4, 3.5, 3, 2.5, 2, 1.5, 1, 0 };
        for (Student stu : students) {
            int j = 0;
            for (Course course : courses) {
                assertEquals(numGrades[j++], stu.getCompletedCoursesGrades().get(course));
            }
        }
    }

    @Test
    public void testpreRequisitesCheckMethod() {
        testEnterGradesMethod();
        for (int i = 0; i < courses.size(); i++) {
            assertTrue(students.get(i).preRequisitesCheck(courses.get(i)));
        }
    }

    @Test
    public void testCalculateGpaMethod() {
        testEnterGradesMethod();
        for (Student stu : students) {
            //System.out.println(stu.calculateGPA());
            assertEquals(2.1875, stu.calculateGPA());
        }
    }

    @Test
    public void testgetGpaStatusMethod() {
        testCalculateGpaMethod();
        for (Student stu : students) {
            assertEquals(Student.GPAstatus.NORMAL, stu.getGpaStatus());
        }
    }
}
