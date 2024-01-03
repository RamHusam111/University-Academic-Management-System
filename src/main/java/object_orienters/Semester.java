package object_orienters;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Semester {

    private String name; // name of the semester
    private String semesterName; // name + year
    private LocalDate semesterStartDate;
    private LocalDate semesterEndDate;
    private Set<Student> students;
    private Set<Teacher> teachers;
    private Set<Course> courses;
    private final long weeksNumber;
    private boolean isFall;
    private boolean isSpring;
    private boolean isSummer;

    /**
     * Constructs a new Semester with specified start and end dates.
     * Initializes the semester name, calculates its length in weeks, and sets flags
     * for
     * whether it's a Fall, Spring, or Summer semester.
     *
     * @param semesterStartDate The start date of the semester.
     * @param semesterEndDate   The end date of the semester.
     */

    public Semester(LocalDate semesterStartDate, LocalDate semesterEndDate) {

        this.semesterStartDate = semesterStartDate;
        this.semesterEndDate = semesterEndDate;
        this.name = giveName();
        this.semesterName = this.name + " - " + semesterStartDate.getYear();
        this.students = new HashSet<>();
        this.teachers = new HashSet<>();
        this.courses = new HashSet<>();
        this.isFall = this.name.equals("Fall");
        this.isSpring = this.name.equals("Spring");
        this.isSummer = this.name.equals("Summer");
        this.weeksNumber = calculateWeeksBetween(semesterStartDate, semesterEndDate);

    }

    /**
     * Registers a course for the semester, including assigning a teacher and
     * enrolling students.
     * Checks if 2 courses have the same ID, room conflicts, teacher availability, and student prerequisites.
     * Only enrolls students who meet all criteria.
     *
     * @param course    The course to be registered.
     * @param lStudents The list of students attempting to register for the course.
     * @param teacher   The teacher assigned to the course.
     */
    // TESTED SUCCESSFULLY
    public void registerInACourse(Course course, List<Student> lStudents, Teacher teacher) {
        // Check if 2 courses have the same ID
        if (courses.stream().anyMatch(e -> e.getCourseID().equalsIgnoreCase(course.getCourseID()))) {
            System.out.println("There's already a course with the ID: " + course.getCourseID()
                    + " cannot have 2 courses with the same ID.");
            return;
        }
        // Check for room conflict
        boolean roomConflict = courses.stream()
                .flatMap(e -> e.getWeeklyMeetings().stream())
                .anyMatch(wm -> course.getWeeklyMeetings().stream().anyMatch(wm2 -> wm2.hasRoomConflict(wm)));

        if (roomConflict) {
            System.out.println(
                    "Error registering " + course.getCourseName() + " because another course has conflict with room");
            return;
        }

        // Check if teacher is free
        if (!teacher.isFreeOn(course.getWeeklyMeetings())) {
            System.out.println("Error registering " + course.getCourseName()
                    + " because teacher has conflict with course Weekly Meetings");
            return;
        }

        // Check if prerequisites are met
        lStudents.stream().filter(e -> !e.preRequisitesCheck(course)).forEach(student -> {
            System.out.println("Prerequisites need to be completed for " + student.getId() + ": "
                    + student.getName() + "> to register in " + course.getCourseName());
        });

        // Check if student is free on weekly meetings
        lStudents.stream().filter(e -> !e.isFreeOn(course.getWeeklyMeetings())).forEach(student -> {
            System.out.println("Error registering " + student.getId() + " " + student.getName() + " in "
                    + course.getCourseName() + " because of conflict");
        });

        // Register students who meet all criteria
        List<Student> registeredStudents = lStudents.stream()
                .filter(student -> student.isFreeOn(course.getWeeklyMeetings()) && student.preRequisitesCheck(course))
                .collect(Collectors.toList());
        registeredStudents.stream().forEach(student -> {
            // Use enrollStudent method of Course class to add student
            course.enrollStudent(student);
            if (!course.isFull()) {
                student.addRegisteredCourse(course);
                System.out.println(
                        student.getId() + " " + student.getName() + " registered in " + course.getCourseName());
                courses.add(course);
                course.setTeacher(teacher);
                teacher.getRegisteredCourses().add(course);
                this.students.add(student);
                this.teachers.add(teacher);
            }
        });

        // Set up the course with the teacher and add to courses list

    }

    /**
     * Determines the name of the semester (Fall, Spring, or Summer) based on its
     * start date.
     *
     * @return The name of the semester.
     */
    public String giveName() {
        int startMonth = semesterStartDate.getMonthValue();
        if ((startMonth >= 9 && startMonth <= 12)) {
            // Fall semester (September to December)
            this.setName("Fall");
        } else if ((startMonth >= 1 && startMonth <= 6)) {
            // Spring semester (January to May)
            this.setName("Spring");
        } else if ((startMonth >= 6 && startMonth <= 8)) {
            // Summer semester (June to August)
            this.setName("Summer");
        }

        return name;
    }

    // HELPER NETHOD
    private void setName(String name) {
        this.name = name;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public long getWeeksNumber() {
        return weeksNumber;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public Set<Course> getCourse() {
        return courses;
    }

    public LocalDate getSemesterStartDate() {
        return semesterStartDate;
    }

    public LocalDate getSemesterEndDate() {
        return semesterEndDate;
    }

    public boolean isFall() {
        return isFall;
    }

    public boolean isSpring() {
        return isSpring;
    }

    public boolean isSummer() {
        return isSummer;
    }

    /**
     * Calculates the number of weeks between two dates(Start date and End date).
     *
     * @param startDate The start date.
     * @param endDate   The end date.
     * @return The number of weeks between the two dates.
     */
    static long calculateWeeksBetween(LocalDate startDate, LocalDate endDate) {
        // Calculate the number of weeks in the Semester
        return ChronoUnit.WEEKS.between(startDate, endDate);
    }

    /**
     * Returns a string representation of the semester.
     * This representation includes the semester's name, start date, and end date,
     * providing a concise summary of the semester's duration and identity.
     *
     * @return A formatted string containing the semester's details.
     */
    @Override
    public String toString() {
        return "Semester: " + semesterName + "[from:" + semesterStartDate + ", to:" + semesterEndDate + "]";
    }

}
