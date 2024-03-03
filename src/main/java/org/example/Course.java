package org.example;

import java.util.*;

/**
 * Represents a course offered by the university.
 * A course can be a major requirement, minor requirement, or university
 * requirement.
 * A course can have a teacher assigned to it, and can have a list of
 * prerequisites.
 * A course can also have a list of weekly meetings, which are the days and times
 * that the course meets.
 * A course can have a maximum capacity of students that can be enrolled.
 * It also holds the number of credit hours the course offers.
 * And Finally, a course can have a list of students enrolled in it.
 */
public class Course {
    private final int CAPACITY;
    private Type courseType;
    private Faculty courseFaculty;
    private String courseID;
    private String courseName;
    private int creditHours;
    private Set<Course> preRequisites;
    private List<WeeklyMeeting> weeklyMeetings;
    private Teacher teacher;
    private List<Student> enrolledStudents;

    /**
     * Constructor for university requirement courses.
     *
     * @param courseID       The unique identifier for the course.
     * @param courseName     The name of the course.
     * @param courseFaculty  The faculty to which the course belongs.
     * @param creditHours    The number of credit hours the course offers.
     * @param weeklyMeetings The weekly meetings scheduled for the course.
     * @param CAPACITY       The maximum number of students that can be enrolled.
     */
    public Course(String courseID, String courseName, Faculty courseFaculty, int creditHours,
            List<WeeklyMeeting> weeklyMeetings, int CAPACITY) {
        this.enrolledStudents = new ArrayList<>();
        this.courseID = courseID;
        this.courseFaculty = courseFaculty;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.preRequisites = new HashSet<Course>();
        this.weeklyMeetings = weeklyMeetings;
        this.CAPACITY = CAPACITY; // Initialize the capacity field
        courseType = Type.UNIVERSITY_REQUIREMENT;
    }

    /**
     * Constructor for major/minor requirement courses.
     *
     * @param courseID       The unique identifier for the course.
     * @param courseName     The name of the course.
     * @param specialization The specialization that this course falls under.
     * @param creditHours    The number of credit hours the course offers.
     * @param weeklyMeetings The weekly meetings scheduled for the course.
     * @param CAPACITY       The maximum number of students that can be enrolled.
     */
    public Course(String courseID, String courseName, Specialization specialization, int creditHours,
            List<WeeklyMeeting> weeklyMeetings, int CAPACITY) {
        this.enrolledStudents = new ArrayList<>();

        this.courseID = courseID;
        this.courseFaculty = specialization.getFaculty();
        courseFaculty.addMajorCourse(this);
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.preRequisites = new HashSet<>();
        this.weeklyMeetings = weeklyMeetings;
        this.CAPACITY = CAPACITY; // Initialize the capacity field
        courseType = specialization.getType() == Specialization.Type.MAJOR ? Type.MAJOR_REQUIREMENT
                : Type.MINOR_REQUIREMENT;
    }

    /**
     * Enrolls a student in the course if there is available capacity.
     * If the course is full, enrollment is not possible and an appropriate message
     * is displayed.
     *
     * @param student The student to be enrolled in the course.
     */
    // Method to enroll a student in the course
    public void enrollStudent(Student student) {
        if (!isFull()) {
            enrolledStudents.add(student);
        } else {
            System.out.println("Course is full. Cannot enroll student: " + student.getName());
        }
    }

    /**
     * Checks if the course has reached its maximum capacity of enrolled students.
     *
     * @return true if the number of enrolled students equals or exceeds the
     *         capacity,
     *         false otherwise.
     */
    // Method to check if the course is full
    public boolean isFull() {
        return enrolledStudents.size() >= this.getCapacity();
    }

    /**
     * Returns a string representation of the course, including details such as
     * course type, course name, and credit hours.
     *
     * @return A formatted string containing key information about the course.
     */
    @Override
    public String toString() {
        return "Course ID: " + this.getCourseID() + "\nCourse Name: " + this.getCourseName() +
                "\nCredit Hours: " + this.getCourseName() + "\nCapacity: " + this.getCapacity() + "\nCourse Faculty: "
                + this.getCourseFaculty().getName() + "\nPrerequisites: [" + this.getPrerequisites().stream().map(e -> e.getCourseID() + ", ").reduce("", String::concat)
                + "]\nCourse Type: "
                + this.getCourseType();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Course) {
            return this.getCourseID().equals(((Course) obj).getCourseID());
        }
        return false;
    }

    /**
     * Retrieves the unique identifier for the course.
     *
     * @return The course ID.
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * Retrieves the type of the course.
     *
     * @return The course type, which indicates whether it is a major requirement,
     *         university requirement, or minor requirement.
     */
    public Type getCourseType() {
        return courseType;
    }

    /**
     * Retrieves the faculty to which the course is associated.
     *
     *
     * @return The faculty associated with the course.
     */
    public Faculty getCourseFaculty() {
        return courseFaculty;
    }

    /**
     * Retrieves the list of students enrolled in the course.
     *
     * @return A list of students enrolled in the course.
     */
    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    /**
     * Retrieves the name of the course.
     *
     * @return The name of the course.
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Retrieves the number of credit hours the course offers.
     *
     * @return The number of credit hours the course offers.
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Retrieves the number of credit hours the course offers.
     *
     * @return The number of credit hours the course offers.
     */
    public int getCreditHours() {
        return creditHours;
    }

    /**
     * Retrieves the teacher assigned to the course.
     *
     * @return An Optional containing the teacher if one is assigned, or an empty
     *         Optional
     *         if no teacher is assigned to the course.
     */
    public Optional<Teacher> getTeacher() {
        return Optional.ofNullable(teacher);
    }

    /**
     * Assigns a teacher to the course.
     *
     * @param teacher The teacher to be assigned to the course.
     */
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    /**
     * Retrieves the list of prerequisites for the course.
     *
     * @return A Set of courses that are prerequisites for the course.
     */
    public Set<Course> getPrerequisites() {
        return preRequisites;
    }

    /**
     * Adds a prerequisite to the course.
     *
     * @param preRequisite The course to be added as a prerequisite.
     */
    public void addPrerequisites(Course preRequisite) {
        this.preRequisites.add(preRequisite);
    }

    /**
     * Retrieves the list of weekly meetings for the course.
     *
     * @return A list of weekly meetings for the course.
     */
    public List<WeeklyMeeting> getWeeklyMeetings() {
        return weeklyMeetings;
    }

    /**
     * Sets the list of weekly meetings for the course.
     *
     * @param weeklyMeetings The list of weekly meetings to be set for the course.
     */
    public void setWeeklyMeetings(List<WeeklyMeeting> weeklyMeetings) {
        this.weeklyMeetings = weeklyMeetings;
    }

    /**
     * Retrieves the maximum number of students that can be enrolled in the course.
     *
     * @return The maximum number of students that can be enrolled in the course.
     */
    public int getCapacity() {
        return CAPACITY;
    }

    /**
     * Enumeration representing the different types of courses.
     * This helps categorize courses based on their requirement type such as
     * major requirement, minor requirement, or university requirement.
     */
    public enum Type {
        MAJOR_REQUIREMENT, UNIVERSITY_REQUIREMENT, MINOR_REQUIREMENT;
    }

}