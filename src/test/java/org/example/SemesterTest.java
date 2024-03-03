package org.example;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SemesterTest {
    private LocalDate start;
    private LocalDate end;
    private List<Semester> semesters;
    private Semester semester;
    private Semester semester44;

    @Before
    public void setUp() throws IOException {
        String filePath = "src/test/resources/SemestersDates.csv";
        semesters = Files.lines(Paths.get(filePath))
                // .skip(1) // Skipping the header row
                .map(line -> line.split(","))
                .map(parts -> new Semester(
                        LocalDate.parse(parts[0].trim()),
                        LocalDate.parse(parts[1].trim())))
                .collect(Collectors.toList());
        semester = semesters.get(0);
        semester44 = semesters.get(44);
        start = LocalDate.of(2023, 9, 1);
        end = LocalDate.of(2023, 12, 31);

    }

    @Test
    public void testGiveName() {
        Class<?> clazz = semester.getClass();
        Arrays.stream(clazz.getDeclaredMethods()).filter(m -> m.getName().equals("giveName")).forEach(m -> {
            try {
                m.setAccessible(true);
                assertEquals("Fall", m.invoke(semester));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testCalculateWeeksBetween() {
        long expectedWeeks = 17;
        assertEquals(expectedWeeks,
                Semester.calculateWeeksBetween(semester.getSemesterStartDate(), semester.getSemesterEndDate()));
    }

    @Test
    public void testSemesterInitialization() {
        assertEquals("Fall - 2023", semester.getSemesterName());
        assertEquals(17, semester.getWeeksNumber());
        assertEquals(start, semester.getSemesterStartDate());
        assertEquals(end, semester.getSemesterEndDate());
    }

    @Test
    public void testRegister2CoursesSameID() {
        Semester semester2 = semesters.get(1);
        List<Course> courses = new ArrayList<>();
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
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        Teacher t = new Teacher("AahmD", spec);
        Student stu = new Student("Angela", spec);
        Course c1 = courses.get(0);
        Course c2 = courses.get(0);
        semester2.registerInACourse(c1, List.of(stu), t);
        semester2.registerInACourse(c2, List.of(stu), t);
        assertTrue(stu.getRegisteredCourses().size() == 1);
    }

    @Test
    public void testRegisterWithRoomConflict() throws IOException {
        List<WeeklyMeeting> weeklyMeetings = new ArrayList<>();
        List<Course> courses = new ArrayList<>();

        int offset = 8;
        BufferedReader brwm = new BufferedReader(new FileReader("src/test/resources/WeeklyMeetings.csv"));
        String line = brwm.readLine();
        while (line != null) {
            // String[] attributes = line.split(",");
            WeeklyMeeting wm = new WeeklyMeeting(DayOfWeek.FRIDAY, Duration.ofMinutes(59), "M-101", LocalTime.of(8, 0));
            weeklyMeetings.add(wm);
            line = brwm.readLine();
        }
        brwm.close();

        BufferedReader br = new BufferedReader(new FileReader("src/test/resources/Courses.csv"));
        line = br.readLine();
        int i = 0;
        while (line != null) {
            String[] attributes = line.split(",");
            String code = (attributes[0].toUpperCase());
            String name = attributes[1];
            Faculty faculty = new Faculty(attributes[2]);
            Course c = new Course(code, name, faculty, 2,
                    List.of(weeklyMeetings.get(offset * i++), weeklyMeetings.get(offset * i++)), 50);
            courses.add(c);
            line = br.readLine();
        }
        br.close();

        i = 0;
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        semester.registerInACourse(courses.get(0), List.of(new Student("Jhon", spec)), new Teacher("AahmD", spec));

        for (Course course : courses) {
            spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
            semester.registerInACourse(course, List.of(new Student("Jhon", spec)), new Teacher("AahmD", spec));
            assertTrue(semester.getTeachers().size() == 1 && semester.getStudents().size() == 1
                    && semester.getRegisteredCourses().size() == 1);
        }

    }

    @Test
    public void testRegisterTeacherIsFree() throws IOException {
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        Teacher t = new Teacher("AahmD", spec);
        assumeTrue(
                t.isFreeOn(new WeeklyMeeting(DayOfWeek.FRIDAY, Duration.ofMinutes(59), "M-101", LocalTime.of(8, 0))));
        semester.registerInACourse(new Course("MATH101", "Calculus I", spec, 2,
                List.of(new WeeklyMeeting(DayOfWeek.FRIDAY, Duration.ofMinutes(59), "M-101", LocalTime.of(8, 0)),
                        new WeeklyMeeting(DayOfWeek.FRIDAY, Duration.ofMinutes(59), "M-101", LocalTime.of(8, 0))),
                50),
                List.of(new Student("Jhon", spec)), t);
        assertTrue(semester.getTeachers().size() == 1 && semester.getStudents().size() == 1
                && semester.getRegisteredCourses().size() == 1);

    }

    @Test
    public void testRegisterPreRequistuiteCoursesFail() {
        Semester semester2 = semesters.get(1);
        List<Course> courses = new ArrayList<>();
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
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        Teacher t = new Teacher("AahmD", spec);
        for (int i = 0; i < courses.size(); i++) {
            if (i != 0) {
                courses.get(i).addPrerequisites(courses.get(i - 1));
            }
        }
        for (int i = 1; i < courses.size(); i++) {
            semester2.registerInACourse(courses.get(i), List.of(new Student("Angela", spec)), t);
            System.out.println("here we start");
            System.out.println(semester2.getStudents());
            assertTrue(semester2.getStudents().isEmpty());
        }

    }

    @Test
    public void testRegisterPreRequistuiteCoursesSuccess() {
        Semester semester2 = semesters.get(1);
        List<Course> courses = new ArrayList<>();
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
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        Teacher t = new Teacher("AahmD", spec);
        Student stu = new Student("Angela", spec);
        for (int i = 0; i < courses.size(); i++) {
            if (i != 0) {
                courses.get(i).addPrerequisites(courses.get(i - 1));
            }
            semester2.registerInACourse(courses.get(i), List.of(stu), t);
            stu.enterCourseGrade(courses.get(i), "A");
        }
        assertFalse(semester2.getStudents().isEmpty());

    }

    @Test
    public void testRegisterCourseCapacityFull() {
        List<Student> students = new ArrayList<>();
        WeeklyMeeting wm = new WeeklyMeeting(DayOfWeek.FRIDAY, Duration.ofMinutes(59), "M-101", LocalTime.of(8, 0));
        Course course = new Course("SWER348", "Advanced OOP", new Faculty("Applied Science and Technology"), 3,
                List.of(wm), 1);
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
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        Teacher t = new Teacher("AahmD", spec);
        Semester semester3 = semesters.get(2);
        semester3.registerInACourse(course, students, t);
        assertTrue(course.getEnrolledStudents().size() == 1);
    }

    @Test
    public void testRegisterCourseCapacityNotFull() {
        List<Student> students = new ArrayList<>();
        WeeklyMeeting wm = new WeeklyMeeting(DayOfWeek.FRIDAY, Duration.ofMinutes(59), "M-101", LocalTime.of(8, 0));
        Course course = new Course("SWER348", "Advanced OOP", new Faculty("Applied Science and Technology"), 3,
                List.of(wm), 200);
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
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        Teacher t = new Teacher("AahmD", spec);
        Semester semester3 = semesters.get(2);
        semester3.registerInACourse(course, students, t);
        assertTrue(course.getEnrolledStudents().size() == 150);
    }

    @Test
    public void testRegisterOptimalScenario() {
        List<Student> students = new ArrayList<>();
        WeeklyMeeting wm = new WeeklyMeeting(DayOfWeek.FRIDAY, Duration.ofMinutes(59), "M-101", LocalTime.of(8, 0));
        Course course = new Course("SWER348", "Advanced OOP", new Faculty("Applied Science and Technology"), 3,
                List.of(wm), 35);
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
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        Teacher t = new Teacher("Angela Salem", spec);
        Semester semester3 = semesters.get(2);
        semester3.registerInACourse(course, students, t);

        assertTrue(course.getEnrolledStudents().size() == 35);
        assertTrue(semester3.getTeachers().size() == 1);
        assertTrue(semester3.getRegisteredCourses().size() == 1);
    }


    //Remove Specific Students from a Course

    @Test
    public void testUnregisterSpecificStudentsFromCourse() {
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        WeeklyMeeting wm = new WeeklyMeeting(DayOfWeek.MONDAY, Duration.ofMinutes(59), "M-101", LocalTime.of(14, 0));
        Course course = new Course("SWER348", "Advanced OOP", new Faculty("Applied Science and Technology"), 3,
                List.of(wm), 200);
        Student student1 = new Student("Alice", new Specialization("Computer Science", new Faculty("Engineering"), Specialization.Type.MAJOR));
        Student student2 = new Student("Bob", new Specialization("Computer Science", new Faculty("Engineering"), Specialization.Type.MAJOR));
        Student student3 = new Student("Husam", new Specialization("Computer Science", new Faculty("Engineering"), Specialization.Type.MAJOR));
        Student student4 = new Student("Yousef", new Specialization("Computer Science", new Faculty("Engineering"), Specialization.Type.MAJOR));
        Teacher teacher = new Teacher("Angela", spec);
        semester44.registerInACourse(course, Arrays.asList(student1, student2), teacher);
        semester44.registerInACourse(course, Arrays.asList(student3, student4), teacher);
        // Unregister specific student(s) from the course
        System.out.println(course.getEnrolledStudents());
        semester44.unregisterInACourse(course, Arrays.asList(student1), false);
        System.out.println(course.getEnrolledStudents());

        // Assertions
        assertFalse( course.getEnrolledStudents().contains(student1));
        assertTrue( course.getEnrolledStudents().contains(student2));
    }


    // Unassign a Teacher from a Course
    @Test
    public void testUnassignTeacherFromCourse() {
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        WeeklyMeeting wm = new WeeklyMeeting(DayOfWeek.MONDAY, Duration.ofMinutes(59), "M-101", LocalTime.of(14, 0));
        Course course = new Course("SWER348", "Advanced OOP", new Faculty("Applied Science and Technology"), 3,
                List.of(wm), 200);
        Student student1 = new Student("Alice",
                new Specialization("Computer Science", new Faculty("Engineering"), Specialization.Type.MAJOR));
        Student student2 = new Student("Bob",
                new Specialization("Computer Science", new Faculty("Engineering"), Specialization.Type.MAJOR));
        Teacher teacher = new Teacher("AahmD", spec);

        semester44.registerInACourse(course, Arrays.asList(student1, student2), teacher);
        semester44.unregisterInACourse(course, List.of(), true);

        assertFalse(course.getTeacher().isPresent());
    }

    // Remove a Course from the Semester
    @Test
    public void testRemoveCourseFromSemester() {
        Specialization spec = new Specialization("maths", new Faculty("Science"), Specialization.Type.MAJOR);
        WeeklyMeeting wm = new WeeklyMeeting(DayOfWeek.MONDAY, Duration.ofMinutes(59), "M-101", LocalTime.of(14, 0));
        Course course = new Course("SWER348", "Advanced OOP", new Faculty("Applied Science and Technology"), 3,
                List.of(wm), 200);
        Student student1 = new Student("Alice",
                new Specialization("Computer Science", new Faculty("Engineering"), Specialization.Type.MAJOR));
        Student student2 = new Student("Bob",
                new Specialization("Computer Science", new Faculty("Engineering"), Specialization.Type.MAJOR));
        Teacher teacher = new Teacher("AahmD", spec);
        semester44.registerInACourse(course, Arrays.asList(student1, student2), teacher);
        semester44.unregisterInACourse(course, Arrays.asList(student1, student2), true);

        assertFalse(semester.getRegisteredCourses().contains(course));
    }

}