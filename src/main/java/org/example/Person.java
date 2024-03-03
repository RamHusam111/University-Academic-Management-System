package org.example;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class representing a person in an educational context.
 * This class is responsible for storing and providing access to information
 * about a person,
 * such as their name, ID, email, role, and schedule.
 * A person can be either a student or a teacher, May be extened to STAFF.
 */
public abstract class Person {
    private int id;
    private String name;
    private String email;
    private Role role = Role.STAFF;
    private Set<Course> registeredCourses;
    private Schedule schedule;
    private LocalDate dateEnrolled; // YOUSEF CHANGED IT FROM yearEnrolled to dateEnrolled
    private static int stuSequence = 1000;
    private static int teacherSequence = 5000;
    private static Year yearValue = Year.of(2023);

    /**
     * Constructs a new Person=> (Teacher or Student) with the specified role and
     * name.
     * Automatically assigns an ID, email, and sets the current date as the
     * enrollment date.
     * Initializes an empty list for registered courses.
     *
     * @param role The role of the person ( STUDENT, TEACHER).
     * @param name The full name of the person.
     */
    public Person(Role role, String name) {
        this.name = name;
        this.role = role;
        dateEnrolled = LocalDate.now();
        registeredCourses = new HashSet<>();
        this.id = this.setID();
        this.email = this.id + "@objectOrienters.com";
        this.schedule = new Schedule(this.getRegisteredCourses());
    }

    /**
     * Generates and sets a unique ID for the person based on their role and
     * enrollment year.
     * This ID is a combination of the year of enrollment and a sequence number,
     * which differs
     * between students and teachers.
     *
     * @return The generated unique ID.
     */

    // TESTED SUCCESSFULLY
    private int setID() {
        String id = "";
        Year enrolledYear = Year.of(dateEnrolled.getYear());
        if (this instanceof Student) {
            if (enrolledYear.isAfter(yearValue)) {
                yearValue = enrolledYear;
                stuSequence = 1000;
            }
            id = yearValue.getValue() + "0" + stuSequence;
            stuSequence++;

        } else if (this instanceof Teacher) {
            if (enrolledYear.isAfter(yearValue)) {
                yearValue = enrolledYear;
                teacherSequence = 1000;
            }
            id = yearValue.getValue() + "5" + teacherSequence;
            teacherSequence++;
        }
        return Integer.parseInt(id);
    }

    /**
     * Registers the student in a given course, if possible.
     * If the person is a student and the course is full, registration is not
     * allowed.
     *
     * @param course The course to register in.
     */

    public void addRegisteredCourse(Course course) {
        // Check if the person is a student and the course is full
        if (this.getRegisteredCourses().stream()
                .anyMatch(e -> e.getCourseID().equalsIgnoreCase(course.getCourseID()))) {
            System.out.println("Cannot register in " + course.getCourseName()
                    + " as the student is already enrolled in a course with the same ID: " + course.getCourseID());
        } else if (this.role == Role.STUDENT && course.isFull()) {
            System.out.println("Cannot register in " + course.getCourseName() + " as the course is already full.");
        } else {
            this.registeredCourses.add(course);
        }
    }

    /**
     * Retrieves the ID of the person.
     *
     * @return The ID of the person.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the role of the person.
     *
     * @return The role of the person.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Retrieves the email of the person.
     *
     * @return The email of the person.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the name of the person.
     *
     * @return The name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the list of courses in which the person is currently registered in a Semester.
     *
     * @return The list of courses in which the person is currently registered in a Semeester.
     */
    public Set<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    /**
     * Retrieves the total credit hours of courses in which the person is enrolled.
     *
     * @return The total credit hours.
     */
    public int getCreditLoad() {
        return schedule.getCreditLoad();
    }


    private Schedule getSchedule() {
        return schedule;
    }

    /**
     * Retrieves the date the person enrolled in the university.
     *
     * @return The date the person enrolled in the university.
     */
    public LocalDate getDateEnrolled() {
        return dateEnrolled;
    }

    /**
     * Displays the person's schedule in a readable format.
     * This method organizes and presents the schedule, showing the timing and
     * details
     * of the classes or appointments the person has.
     *
     *
     * @return A string representation of the person's schedule, formatted for easy
     *         reading.
     *         If the schedule is empty or not set, this method may return an
     *         appropriate
     *         message indicating so.
     */
    public void showSchedule() {
        System.out.println(this.getSchedule().displaySchedule());
    }

    /**
     * Checks if the person is free on a given day and time.
     * This method checks the person's schedule to see if they have any other
     * commitments
     * during the specified list of Weekly Meetings.
     *
     * @param list The list of Weekly Meetings to check.
     * @return true if the person is free, false otherwise.
     */
    public boolean isFreeOn(List<WeeklyMeeting> list) {
        return list.stream().allMatch(e -> isFreeOn(e));
    }

    /**
     * Checks if the person is free on a given day and time.
     * This method checks the person's schedule to see if they have any other
     * commitments
     * during the specified Weekly Meeting.
     *
     * @param weeklyMeeting The Weekly Meeting to check.
     * @return true if the person is free, false otherwise.
     */
    public boolean isFreeOn(WeeklyMeeting weeklyMeeting) {

        return registeredCourses.stream()
                .flatMap(course -> course.getWeeklyMeetings().stream())
                .noneMatch(wm -> {
                    return wm.getDay().equals(weeklyMeeting.getDay())
                            && !(wm.getHour().plus(wm.getDuration()).isBefore(weeklyMeeting.getHour())
                                    || wm.getHour().isAfter(weeklyMeeting.getHour().plus(weeklyMeeting.getDuration())));
                });
    }

    /**
     * Returns a string representation of the Teacher or Student.
     * This method typically includes key attributes of the person, such as name,
     * ID, role,
     * email. It returns summary of the
     * person's information.
     *
     * @return A formatted string representing the person's(Teacher, Student)
     *         details.
     */
    @Override
    public String toString() {
        return "ID: " + this.getId() + "\nName: " + this.getName() + "\nEmail: " + this.getEmail();
    }

    /**
     * Checks if the person is equal to another person.
     * This method compares the ID of the person to the ID of the other person.
     *
     * @param obj The other person to compare to.
     * @return true if the two persons are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return this.getId() == ((Person) obj).getId();
        }
        return false;
    }

    /**
     * Enumeration representing the different roles a person can have in an
     * educational context.
     * This includes roles like STUDENT, TEACHER, STAFF. Each role can be associated
     * with specific privileges, access rights, or behaviors within the system.
     *
     */
    public enum Role {
        STUDENT, TEACHER, STAFF;
    }

    /**
     * A class representing the weekly schedule of a person(Teacher, Student).
     * This class is responsible for organizing and displaying the weekly schedule
     * of a person.
     * The schedule is organized by day, listing each course meeting time and
     * location.
     */
    private class Schedule {
        private Set<Course> courses;
        Map<WeeklyMeeting, Course> meetingCourseMap;

        /**
         * Constructs a new Schedule representation for the person(Teacher, Student) based on the
         * courses in which they are enrolled.
         *
         * @param courses The courses in which the person is enrolled.
         */
        public Schedule(Set<Course> courses) {
            this.courses = courses;
            this.meetingCourseMap = new HashMap<>();
            this.courses.forEach(course -> course.getWeeklyMeetings()
                    .forEach(weeklyMeeting -> meetingCourseMap.put(weeklyMeeting, course)));

        }

        /**
         * Calculates and returns the total credit hours of courses in which the student
         * is enrolled.
         *
         * @return The total credit hours.
         */
        public int getCreditLoad() {
            return courses.stream().mapToInt(e -> e.getCreditHours()).sum();
        }

        /**
         * Generates and returns a string representation of the person's(Teacher,
         * Student) weekly schedule.
         * The schedule is organized by day, listing each course meeting time and
         * location.
         *
         * @return A formatted string of the weekly schedule.
         */
        // Method to display the schedule
        public String displaySchedule() {
            StringBuilder scheduleBuilder = new StringBuilder();

            // Grouping weekly meetings by day
            Map<DayOfWeek, List<WeeklyMeeting>> meetingsByDay = courses.stream()
                    .flatMap(course -> course.getWeeklyMeetings().stream())
                    .collect(Collectors.groupingBy(WeeklyMeeting::getDay));

            // Sorting and formatting schedule by day
            meetingsByDay.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        DayOfWeek day = entry.getKey();
                        List<WeeklyMeeting> meetings = entry.getValue();
                        scheduleBuilder.append(day).append(":\n");

                        meetings.stream()
                                .sorted(Comparator.comparing(WeeklyMeeting::getHour))
                                .forEach(weeklyMeeting -> {
                                    Course course = meetingCourseMap.get(weeklyMeeting);
                                    scheduleBuilder.append(formatMeeting(course, weeklyMeeting)).append("\n");
                                });
                    });

            return scheduleBuilder.toString();
        }

        /**
         * Formats the details of a single meeting for display in a schedule.
         * This method is responsible for creating a string representation of a meeting,
         * including
         * the time, duration, course name, and room information. The output is
         * typically used
         * as part of a larger schedule display.
         *
         * @param course  The course associated with the meeting.
         * @param meeting The meeting to be formatted. This object should include
         *                details such
         *                as the meeting time, duration, and room.
         * @return A formatted string representing the meeting's details, suitable for
         *         inclusion
         *         in a schedule display.
         */

        // Helper method to format a single meeting entry
        private String formatMeeting(Course course, WeeklyMeeting meeting) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            return String.format("  %s - %s | %s | Room: %s",
                    meeting.getHour().format(timeFormatter),
                    meeting.getHour().plus(meeting.getDuration()).format(timeFormatter),
                    course.getCourseName(),
                    meeting.getRoom());
        }
    }
}