package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PersonTest {
    Student stu1;
    Teacher t1;
    Course c1;
    Course c2;
    Course c3;

    List<WeeklyMeeting> weeklyMeetings;

    @BeforeEach
    public void setUp() throws IOException {
        weeklyMeetings = new ArrayList<>(180);
        BufferedReader br = new BufferedReader(new FileReader("src/test/resources/weeklyMeetings.csv"));

        String line = br.readLine();
        while (line != null) {
            String[] attributes = line.split(",");
            DayOfWeek day = DayOfWeek.valueOf(attributes[0].toUpperCase());
            Duration duration = Duration.ofMinutes(Long.parseLong(attributes[1]));
            String room = attributes[2];
            LocalTime hour = LocalTime.parse(attributes[3]);
            WeeklyMeeting wm = new WeeklyMeeting(day, duration, room, hour);
            weeklyMeetings.add(wm);
            line = br.readLine();
        }
        br.close();
        Specialization mathMajor = new Specialization("Maths", new Faculty("Science"), Specialization.Type.MAJOR);
        stu1 = new Student("Alex", mathMajor);
        t1 = new Teacher("Dr. Smith", mathMajor);
        c1 = new Course("MATH101", "Calculus I", mathMajor, 1, List
                .of(new WeeklyMeeting(DayOfWeek.TUESDAY, Duration.ofMinutes(60), "M-301", LocalTime.parse("09:00"))),100);
        new Semester(LocalDate.of(2023, 9, 1), LocalDate.of(2023, 12, 31)).registerInACourse(c1, List.of(stu1), t1);
    }

    @Test
    public void isFreeOnTest() {
        int dayOffset = 37;
        int hourOffset = 4;
        int minsOffset = 0;

        int index = (c1.getWeeklyMeetings().get(0).getDay().getValue() - 1) * dayOffset;
        index += c1.getWeeklyMeetings().get(0).getHour().getHour() % 8 * hourOffset;
        index += c1.getWeeklyMeetings().get(0).getHour().getMinute() % 15 * minsOffset;

        for (int i = 0; i < weeklyMeetings.size(); i++) {

            if (index -4 <= i && i < index + 4) {
                assertTrue(!stu1.isFreeOn(weeklyMeetings.get(i)));
            } else
                assertTrue(stu1.isFreeOn(weeklyMeetings.get(i)));
        }

    }

    @Test
    public void isFreeOnTest2() {


    }

}