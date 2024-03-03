package org.example;

/**
 * Represents a teacher at the university.
 * A teacher can have a specialization, and can teach courses.
 */
public class Teacher extends Person {

  private Specialization specialization;

  /**
   * Constructs a new Teacher with the given name and specialization.
   * Initializes the teacher with the role TEACHER and adds the teacher to the
   * specialization's list of teachers and the faculty's list of teachers.
   *
   * @param name The name of the teacher.
   * @param specialization The area of specialization for the teacher.
   */
  public Teacher(String name, Specialization specialization) {
    super(Role.TEACHER, name);
    this.specialization = specialization;
    specialization.getTeachers().add(this);
    specialization.getFaculty().getTeachers().add(this);

  }
  
  /**
   * Returns the specialization of the teacher.
   *
   * @return The teacher's area of specialization.
   */
  public Specialization getSpecialization() {
    return specialization;
  }


  /**
   * Returns a string representation of the teacher, including their basic information
   * from the Person class and their area of specialization.
   *
   * @return A formatted string with the teacher's details and specialization.
   */
  @Override
  public String toString() {
    return super.toString() + "\nSpecialization: "
        + this.getSpecialization();
  }
}
