package org.example;//package object_orienters;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.DayOfWeek;
//import java.time.Duration;
//import java.time.LocalTime;
//import java.util.Arrays;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ScheduleTest {
//
//    @Test
//    void getCreditLoad() {
//    }
//
//    @Test
//    public void testDisplaySchedule() {
//        // Set up sample teachers
//
//        Teacher teacher1 = new Teacher(1, "Dr. Smith", new Department("Computer Science"), "dr.smith@email.com");
//        Teacher teacher2 = new Teacher(2, "Prof. Johnson", new Department("Mathematics"), "prof.johnson@email.com");
//
//        // Set up sample courses with weekly meetings
//        Course course1 = new Course("Intro to Programming", teacher1, 3,
//                Arrays.asList(new WeeklyMeeting(DayOfWeek.MONDAY, Duration.ofHours(1), "101", LocalTime.of(9, 0)),
//                        new WeeklyMeeting(DayOfWeek.WEDNESDAY, Duration.ofHours(1), "101", LocalTime.of(9, 0))));
//
//        Course course2 = new Course("Calculus I", teacher2, 3,
//                Arrays.asList(new WeeklyMeeting(DayOfWeek.MONDAY, Duration.ofHours(1), "201", LocalTime.of(10, 30)),
//                        new WeeklyMeeting(DayOfWeek.WEDNESDAY, Duration.ofHours(1), "201", LocalTime.of(10, 30))));
//
//        Schedule schedule = new Schedule(Arrays.asList(course1, course2));
//
//        // Expected output
//        String expected = "MONDAY:\n" +
//                "  09:00 - 10:00 | Intro to Programming | Room: 101\n" +
//                "  10:30 - 11:30 | Calculus I | Room: 201\n" +
//                "WEDNESDAY:\n" +
//                "  09:00 - 10:00 | Intro to Programming | Room: 101\n" +
//                "  10:30 - 11:30 | Calculus I | Room: 201\n";
//
//        // Assert that the actual output matches the expected output
//        assertEquals(expected, schedule.displaySchedule());
//    }
//}