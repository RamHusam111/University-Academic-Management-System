package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a faculty at the university.
 * A faculty can have a list of teachers, students, major courses, minor courses,
 * and specializations.
 */

public class Faculty {
    private String name;
    private Set<Teacher> teachers;
    private List<Student> students;
    private List<Course> majorsCourses;
    private List<Course> minorsCourses;
    private List<Specialization> specializations;
    private static List<Faculty> faculties = new ArrayList<>();

    /**
     * Constructs a new Faculty with the given name.
     * Initializes lists for teachers, students, major courses, and minor courses,
     * and adds this Faculty instance to the static list of faculties.
     *
     * @param name The name of the faculty.
     */
    public Faculty(String name) {
        this.name = name;
        this.teachers = new HashSet<>();
        this.students = new ArrayList<>();
        this.majorsCourses = new ArrayList<>();
        this.minorsCourses = new ArrayList<>();
        this.specializations = new ArrayList<>();
        faculties.add(this);

    }

    /**
     * Retrieves the name of the faculty.
     *
     * @return The name of the faculty.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the list of major courses offered by this faculty.
     *
     * @return A list of major courses.
     */

    public List<Course> getMajorsCourses() {
        return majorsCourses;
    }

    /**
     * Retrieves the list of minor courses offered by this faculty.
     *
     * @return A list of minor courses.
     */

    public List<Course> getMinorsCourses() {
        return minorsCourses;
    }

    /**
     * Retrieves the list of teachers associated with this faculty.
     *
     * @return A list of teachers.
     */
    public Set<Teacher> getTeachers() {
        return teachers;
    }

    /**
     * Retrieves the list of students associated with this faculty.
     *
     * @return A list of students.
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Adds a major course to the list of major courses offered by this faculty.
     *
     * @param majorCourse The major course to be added.
     */
    public void addMajorCourse(Course majorCourse) {
        this.majorsCourses.add(majorCourse);
    }

    /**
     * Adds a minor course to the list of minor courses offered by this faculty.
     *
     * @param minorCourse The minor course to be added.
     */
    public void addMinor(Course minorCourse) {
        this.minorsCourses.add(minorCourse);
    }

    /**
     * Returns a string representation of the faculty, including its name and lists
     * of major and minor courses.
     *
     * @return A formatted string containing the faculty's details.
     */
    @Override
    public String toString() {
        return "Faculty name: " + this.getName() + "\nMajor Specializations: " + this.getMajorsCourses()
                + "\nMinor Specializations: " + this.getMinors();
    }

    /**
     * Retrieves the list of Specializations associated with this faculty.
     *
     * @return A list of Specializations.
     */
    public List<Specialization> getSpecializations() {
        return specializations;
    }

    /**
     * Retrieves the list of Major Specialisation assoiciated with this faculty.
     *
     * @return A list of Major Specialisation assoiciated with this faculty.
     */
    public List<Specialization> getMjors() {
        return specializations.stream().filter(specialization -> specialization.getType() == Specialization.Type.MAJOR)
                .toList();
    }

    /**
     * Retrieves the list of Minor Specialisation assoiciated with this faculty.
     *
     * @return A list of Minor Specialisation assoiciated with this faculty.
     */
    public List<Specialization> getMinors() {
        return specializations.stream().filter(specialization -> specialization.getType() == Specialization.Type.MINOR)
                .toList();
    }

}
