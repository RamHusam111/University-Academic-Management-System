package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specialization offered by the university.
 * A specialization can be a major or a minor.
 * A specialization can have a list of teachers assigned to it.
 */
public class Specialization {
    private String name;
    private Faculty faculty;
    private Type type;
    private List<Teacher> teachers;

    /**
     * Constructs a new Specialization with the given name, faculty, and type.
     * Initializes an empty list of teachers.
     *
     * @param name    The name of the specialization.
     * @param faculty The faculty to which the specialization belongs.
     * @param type    The type of the specialization, either MAJOR or MINOR.
     */
    public Specialization(String name, Faculty faculty, Type type) {
        this.teachers = new ArrayList<>();
        this.name = name;
        this.faculty = faculty;
        faculty.getSpecializations().add(this);
        this.type = type;
    }

    /**
     * Retrieves the faculty associated with this specialization.
     *
     * @return The faculty to which this specialization belongs.
     */
    public Faculty getFaculty() {
        return faculty;
    }

    /**
     * Retrieves the name of the specialization.
     *
     * @return The name of the specialization.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the type of the specialization.
     *
     * @return The type of the specialization, either MAJOR or MINOR.
     */
    public Type getType() {
        return type;
    }

    /**
     * Enumeration representing the type of specialization, whether it is a major or
     * a minor.
     */
    public enum Type {
        MAJOR, MINOR;
    }

    /**
     * Retrieves the list of teachers associated with this specialization.
     *
     * @return A list of teachers teaching courses in this specialization.
     */
    public List<Teacher> getTeachers() {
        return this.teachers;
    }

    /**
     * Returns a string representation of the specialization.
     *
     * @return A string containing the name of the specialization.
     */
    public String toString() {
        return "Specialization Name: " + this.getName() + "\nSpecialization Faculty: " + this.getFaculty().getName()
                + "\nSpecialization Type: " + this.getType();
    }
}