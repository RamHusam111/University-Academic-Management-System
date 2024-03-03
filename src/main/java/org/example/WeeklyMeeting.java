package org.example;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

/**
 * Represents a weekly meeting of a course.
 * A weekly meeting has a day, duration, room, and hour.
 */
public class WeeklyMeeting {
    private DayOfWeek day;
    private Duration duration;
    private String room;
    private LocalTime hour;
    private String id;
    private String timeId;

    /**
     * Constructs a new WeeklyMeeting with the given day, duration, room, and hour.
     *
     * @param day      The day of the weekly meeting.
     * @param duration The duration of the weekly meeting.
     * @param room     The room of the weekly meeting.
     * @param hour     The hour of the weekly meeting.
     */
    public WeeklyMeeting(DayOfWeek day, Duration duration, String room, LocalTime hour) {
        this.day = day;
        this.duration = duration;
        this.room = room;
        this.hour = hour;
        this.timeId = day.name() +
                " " + duration.toMinutes() +
                " " + hour.toString();

        this.id = timeId + " " + room;
       
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalTime getHour() {
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * Checks if the weekly meeting has a time conflict with another weekly meeting.
     *
     * @param weeklyMeeting The other weekly meeting.
     * @return True if the weekly meetings have a time conflict, false otherwise.
     */    
    public boolean hasTimeConflict(WeeklyMeeting weeklyMeeting) {
        return this.getDay().equals(weeklyMeeting.getDay())
                   && !(weeklyMeeting.getHour().plus(weeklyMeeting.getDuration()).isBefore(weeklyMeeting.getHour())
                 || weeklyMeeting.getHour().isAfter(weeklyMeeting.getHour().plus(weeklyMeeting.getDuration())));
    }

 

    @Override
    public String toString() {

        return "Weekly Meeting:>" + day.name() +
                " " + duration.toMinutes() +
                " " + hour.toString() + " " + room;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WeeklyMeeting))
            return false;
        return this.id.equals(((WeeklyMeeting) o).id);
    }

    public boolean equalsTime(WeeklyMeeting wM) {
        if (!(wM instanceof WeeklyMeeting))
            return false;
        return this.timeId.equals(((WeeklyMeeting) wM).timeId);
    }

    /**
     * Checks if the weekly meeting has a room conflict with another weekly meeting.
     *
     * @param weeklyMeetings The other weekly meeting.
     * @return True if the weekly meetings have a room conflict, false otherwise.
     */
    public boolean hasRoomConflict(WeeklyMeeting weeklyMeetings) {
        return this.getDay().equals(weeklyMeetings.getDay())
                && !this.getHour().plus(this.getDuration()).isBefore(weeklyMeetings.getHour())
                && !weeklyMeetings.getHour().plus(weeklyMeetings.getDuration()).isBefore(this.getHour())
                && this.getRoom().equals(weeklyMeetings.getRoom());
    }
}
