package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class WeeklyMeetingTest {
    private List<WeeklyMeeting> meetings;
    private WeeklyMeeting meeting1;
    private WeeklyMeeting meeting2;
    private WeeklyMeeting meeting3;
    private WeeklyMeeting meeting4;

    @BeforeEach
    void setUp() throws Exception {
        Path path = Paths.get("src/test/resources/WeeklyMeetings2.csv");
        meetings = Files.lines(path)
                .map(line -> line.split(","))
                .map(parts -> new WeeklyMeeting(
                        DayOfWeek.valueOf(parts[0].trim().toUpperCase()),
                        Duration.ofMinutes(Long.parseLong(parts[1].trim())),
                        parts[2].trim(),
                        LocalTime.parse(parts[3].trim())
                ))
                .collect(Collectors.toList());
        meeting1 = meetings.get(0);
        meeting2 = meetings.get(1);
        meeting3 = meetings.get(2);
        meeting4 = meetings.get(3);


    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(DayOfWeek.MONDAY, meeting1.getDay());
        assertEquals(Duration.ofMinutes(120), meeting1.getDuration());
        assertEquals("101A", meeting1.getRoom());
        assertEquals(LocalTime.of(13, 0), meeting1.getHour());
    }

    @Test
    void testSetters() {
        meeting4.setDay(DayOfWeek.WEDNESDAY);
        meeting4.setDuration(Duration.ofMinutes(120));
        meeting4.setRoom("201A");
        meeting4.setHour(LocalTime.of(12, 0));

        assertEquals(DayOfWeek.WEDNESDAY, meeting4.getDay());
        assertEquals(Duration.ofMinutes(120), meeting4.getDuration());
        assertEquals("201A", meeting4.getRoom());
        assertEquals(LocalTime.of(12, 0), meeting4.getHour());
    }

    @Test
    void testToString() {

        String expectedString = "Weekly Meeting:>MONDAY 120 13:00 101A";
        assertEquals(expectedString, meeting1.toString());
    }

    @Test
    void testEquals() {
//        WeeklyMeeting meeting1 = new WeeklyMeeting(DayOfWeek.MONDAY, Duration.ofHours(2), "101", LocalTime.of(10, 0));
//        WeeklyMeeting meeting2 = new WeeklyMeeting(DayOfWeek.MONDAY, Duration.ofHours(2), "101", LocalTime.of(10, 0));
//        WeeklyMeeting meeting3 = new WeeklyMeeting(DayOfWeek.TUESDAY, Duration.ofHours(1), "102", LocalTime.of(11, 0));

        assertEquals(meeting1, meeting2);
        assertNotEquals(meeting1, meeting3);
    }

    @Test
    void testEqualsTime() {
//        WeeklyMeeting meeting1 = new WeeklyMeeting(DayOfWeek.MONDAY, Duration.ofHours(2), "101", LocalTime.of(10, 0));
//        WeeklyMeeting meeting2 = new WeeklyMeeting(DayOfWeek.MONDAY, Duration.ofHours(2), "102", LocalTime.of(10, 0));

        assertTrue(meeting1.equalsTime(meeting2));
    }

    @Test
    void testHasRoomConflict() {
        WeeklyMeeting meeting2 = new WeeklyMeeting(DayOfWeek.MONDAY, Duration.ofMinutes(60), "101A", LocalTime.of(14, 0));


        assertTrue(meeting1.hasRoomConflict(meeting2));
        assertFalse(meeting1.hasRoomConflict(meeting3));
    }
}