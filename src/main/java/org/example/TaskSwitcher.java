package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * A class that switches between different tasks.
 * depending on the registar input.
 */
public class TaskSwitcher {

    private static Faculty createFaculty(String facultyName) {
        Faculty faculty = RegistrarDriver.faculties.get(
                facultyName);
        if (faculty == null) {
            faculty = new Faculty(facultyName);
            RegistrarDriver.faculties.put(facultyName, faculty);
        }
        return faculty;
    }

    private static Specialization createMajorSpecialization(String specialization) {
        if (RegistrarDriver.specializations.get(specialization) != null) {
            return RegistrarDriver.specializations.get(specialization);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String facultyName = null;
        try {
            System.out.println(Color.YELLOW.value + "\nEnter Major's Faculty Name: " + Color.RESET.value);
            facultyName = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Specialization spec = new Specialization(specialization, createFaculty(facultyName),
                Specialization.Type.MAJOR);
        RegistrarDriver.specializations.put(spec.getName(), spec);
        return spec;
    }

    private static Specialization createMinorSpecialization(String specialization) {
        if (RegistrarDriver.specializations.get(specialization) != null) {
            return RegistrarDriver.specializations.get(specialization);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String facultyName = null;
        try {
            System.out.println(Color.YELLOW.value + "\nEnter Minor's Faculty Name: " + Color.RESET.value);
            facultyName = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Specialization spec = new Specialization(specialization, createFaculty(facultyName),
                Specialization.Type.MINOR);
        RegistrarDriver.specializations.put(spec.getName(), spec);
        return spec;
    }

    private static Student createStudent(Integer id) {
        Student student = RegistrarDriver.students.get(id);
        if (student != null)
            return student;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(Color.YELLOW.value + "Enter Student Name: " + Color.RESET.value);
        String name = null;
        try {
            name = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String majorString = null;
        while (majorString == null) {
            System.out.println(Color.YELLOW.value +
                    "\nEnter Student Major:\n(or type 'show' to check available specializations)\nEnter any string to create a new major"
                    + Color.RESET.value);

            try {
                majorString = br.readLine();
                if (majorString.trim().toLowerCase().equals("show")) {
                    System.out.println(Color.YELLOW.value + "Available Specializations: " + Color.RESET.value);
                    System.out.println(Color.YELLOW.value + RegistrarDriver.specializations.values().stream()
                            .filter(s -> s.getType() == Specialization.Type.MAJOR).map(s -> s.getName())
                            .reduce((s1, s2) -> s1 + "\n-----------------------------------\n" + s2).get()
                            + Color.RESET.value);

                    majorString = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String minorString = null;
        while (true) {
            System.out.println(Color.YELLOW.value +
                    "\nEnter Student Minor or Write (null) if not applicable:\n(or type 'show' to check available specializations)\nEnter any string to create a new minor"
                    + Color.RESET.value);

            try {
                minorString = br.readLine();
                if (minorString.trim().toLowerCase().equals("null")) {
                    minorString = null;
                    break;
                } else if (minorString.trim().toLowerCase().equals("show")) {
                    System.out.println(Color.YELLOW.value + "Available Specializations: " + Color.RESET.value);
                    System.out.println(Color.YELLOW.value + RegistrarDriver.specializations.values().stream()
                            .filter(s -> s.getType() == Specialization.Type.MINOR).map(s -> s.getName())
                            .reduce((s1, s2) -> s1 + "\n-----------------------------------\n" + s2)
                            .orElse("No Minors Available\n") + Color.RESET.value);
                    minorString = null;
                } else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Specialization major = createMajorSpecialization(majorString);
        Specialization minor = null;
        if (minorString != null)
            minor = createMinorSpecialization(minorString);

        System.out.println(Color.YELLOW.value + RegistrarDriver.specializations.get(majorString) + Color.RESET.value);

        student = new Student(name, major, minor);
        RegistrarDriver.students.put(student.getId(), student);
        return student;
    }

    private static Teacher createTeacher(Integer id) {
        Teacher teacher = RegistrarDriver.teachers.get(id);
        if (teacher != null)
            return teacher;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(Color.YELLOW.value + "Enter Teacher Name: " + Color.RESET.value);
        String name = null;
        try {
            name = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Color.YELLOW.value + "\n\nEnter Teacher's Specialization: " + Color.RESET.value);
        String specString = null;
        try {
            specString = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        teacher = new Teacher(name,
                createMajorSpecialization(specString));
        RegistrarDriver.teachers.put(teacher.getId(), teacher);

        return teacher;

    }

    public static Semester createSemester() {
        System.out.println(Color.YELLOW.value + "Creating Semester: " + Color.RESET.value);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        LocalDate startDate = null;
        LocalDate endDate = null;
        while (startDate == null) {

            try {
                System.out.println(Color.YELLOW.value + "Enter Semester Start Date (dd/MM/yyyy);" + Color.RESET.value);
                startDate = LocalDate.parse(br.readLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DateTimeParseException e) {
                System.out.println(Color.YELLOW.value + "Invalid date format. Please enter date in dd/MM/yyyy format"
                        + Color.RESET.value);
            }
        }
        while (endDate == null) {

            try {
                System.out.println(Color.YELLOW.value + "Enter Semester End Date (dd/MM/yyyy):" + Color.RESET.value);
                endDate = LocalDate.parse(br.readLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DateTimeParseException e) {
                System.out.println(Color.YELLOW.value + "Invalid date format. Please enter date in dd/MM/yyyy format"
                        + Color.RESET.value);
            }
        }
        if (startDate.isAfter(endDate)) {
            System.out.println(
                    Color.YELLOW.value + "Start Date is after End Date. Please enter valid dates" + Color.RESET.value);
            return createSemester();
        }

        Semester semester = new Semester(startDate,
                endDate);

        if (RegistrarDriver.semesters.get(semester.getSemesterName()) != null) {
            System.out.println(Color.YELLOW.value + "Semester already exists" + Color.RESET.value);
            return RegistrarDriver.semesters.get(semester.getSemesterName());
        } else {
            RegistrarDriver.semesters.put(semester.getSemesterName(), semester);
            System.out
                    .println(Color.YELLOW.value + "Semester" + semester + " created successfully" + Color.RESET.value);
            return semester;
        }
    }

    private static List<WeeklyMeeting> createWeeklyMeeting() {
        List<WeeklyMeeting> weeklyMeetings = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        WeeklyMeeting wm = null;
        try {
            do {
                System.out.println(Color.YELLOW.value + "Creating Course Weekly Meetings: " + Color.RESET.value);
                DayOfWeek day = null;
                while (day == null) {
                    try {
                        System.out.println(Color.YELLOW.value + "\n\nEnter Meeting Day: " + Color.RESET.value);
                        day = DayOfWeek.valueOf(br.readLine().toUpperCase());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        System.out.println(
                                Color.YELLOW.value + "\n\nInvalid day. Please enter a valid day" + Color.RESET.value);
                    }
                }

                LocalTime startTime = null;
                while (startTime == null) {
                    try {
                        System.out.println(
                                Color.YELLOW.value + "\n\nEnter Meeting Start Time (HH:mm): " + Color.RESET.value);
                        startTime = LocalTime.parse(br.readLine(), DateTimeFormatter.ofPattern("HH:mm"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DateTimeParseException e) {
                        System.out.println(Color.YELLOW.value
                                + "\n\nInvalid time format. Please enter time in HH:mm format" + Color.RESET.value);
                    }
                }

                Duration duration = null;
                while (duration == null) {
                    try {
                        System.out.println(
                                Color.YELLOW.value + "\n\nEnter Meeting Duration (in minutes): " + Color.RESET.value);
                        duration = Duration.ofMinutes(Long.parseLong(br.readLine()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        System.out.println(
                                Color.YELLOW.value + "\n\nInvalid input. Please enter a number" + Color.RESET.value);
                    }
                }

                System.out.println(Color.YELLOW.value + "\n\nEnter Weekly Meeting Room: " + Color.RESET.value);
                String room = null;
                try {
                    room = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                wm = new WeeklyMeeting(day, duration, room, startTime);
                final WeeklyMeeting wm2 = wm;
                if (weeklyMeetings.stream().anyMatch(e -> e.hasTimeConflict(wm2))) {
                    System.out.println(Color.YELLOW.value + "\n\nCannot add a Weekly Meeting on day " + wm2.getDay()
                            + " and time " + wm2.getHour() + " for "+ wm2.getDuration() + " minutes because of conflict with existing weekly meetings."
                            + Color.RESET.value);
                    wm = null;
                    continue;

                }
                weeklyMeetings.add(wm);
                System.out.println(Color.YELLOW.value + "\n\nDo you want to add another weekly meeting? (y/n)"
                        + Color.RESET.value);

            } while (wm == null || br.readLine().toLowerCase().equals("y"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return weeklyMeetings;
    }

    private static Course createCourse() {
        System.out.println(Color.YELLOW.value + "Creating Course Process: " + Color.RESET.value);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(Color.YELLOW.value + "Enter Course Code: " + Color.RESET.value);
        String courseID = null;
        try {
            courseID = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Optional<Course> course = Optional.ofNullable(RegistrarDriver.courses.get(courseID));
        if (course.isPresent()) {
            System.out.println(Color.YELLOW.value + "\nCourse exists\n\n" + Color.RESET.value);
            System.out.println(Color.YELLOW.value + course.get() + Color.RESET.value);
            System.out.println();
            return course.get();
        }

        System.out.println(Color.YELLOW.value + "\n\nEnter Course Name:" + Color.RESET.value);
        String name = null;
        try {
            name = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Color.YELLOW.value + "\n\nEnter Course Specialization: " + Color.RESET.value);
        String specString = null;
        try {
            specString = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Specialization specialization = createMajorSpecialization(specString);

        int creditHours = 0;
        while (creditHours == 0) {
            try {
                System.out.println(Color.YELLOW.value + "\n\nEnter Course Credit Hours number: " + Color.RESET.value);
                creditHours = Integer.parseInt(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println(Color.YELLOW.value + "\n\nInvalid input. Please enter a number" + Color.RESET.value);
            }
        }

        int capacity = 0;
        while (capacity == 0) {
            try {
                System.out
                        .println(Color.YELLOW.value + "\n\nEnter Course Student Capacity number: " + Color.RESET.value);
                capacity = Integer.parseInt(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println(Color.YELLOW.value + "\n\nInvalid input. Please enter a number" + Color.RESET.value);
            }
        }

        List<WeeklyMeeting> weeklyMeetings = createWeeklyMeeting();

        // TODO: check for conflicts between weekly meetings.

        Course c = new Course(courseID, name, specialization, creditHours, weeklyMeetings, capacity);
        RegistrarDriver.courses.put(c.getCourseID(), c);
        System.out.println(Color.YELLOW.value + "Course " + c + " created successfully" + Color.RESET.value);
        return c;

    }

    ////////////////////////////////////////////////////////////////////////////

    private static Runnable action6 = () -> {
        System.out.println(Color.YELLOW.value + "View Course Prerequisites" + Color.RESET.value);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(Color.YELLOW.value + "Enter Course Code: " + Color.RESET.value);
        try {

            final String courseID = br.readLine();
            Course course = RegistrarDriver.courses.values().stream().filter(c -> c.getCourseID().equals(courseID))
                    .findFirst().orElse(null);
            if (course == null) {
                System.out.println(Color.YELLOW.value + "Course not found" + Color.RESET.value);
            } else {
                System.out.println(Color.YELLOW.value + "\n\nCourse Prerequisites:" + Color.RESET.value);
                System.out.println(Color.YELLOW.value +
                        course.getPrerequisites().stream().map(c -> c.getCourseID()).reduce((c1, c2) -> c1 + "\n" + c2)
                                .orElse("No Prerequisites exist for this course")
                        + Color.RESET.value);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    private static Runnable action1 = () -> {
        System.out.println(Color.YELLOW.value + "Create New Semester" + Color.RESET.value);
        createSemester();

    };

    private static Runnable action2 = () -> {
        System.out.println(Color.YELLOW.value + "Create New Student" + Color.RESET.value);
        Student student = createStudent(0);
        System.out.println(Color.YELLOW.value + "Student " + student + " created successfully" + Color.RESET.value);
    };

    private static Runnable action3 = () -> {
        System.out.println(Color.YELLOW.value + "Create New Teacher" + Color.RESET.value);
        Teacher teacher = createTeacher(0);
        System.out.println(Color.YELLOW.value + "Teacher " + teacher + " created successfully" + Color.RESET.value);
    };

    private static Runnable action4 = () -> {
        System.out.println(Color.YELLOW.value + "Create New Course" + Color.RESET.value);
        createCourse();

    };

    private static Runnable action8 = () -> {
        System.out
                .println(Color.YELLOW.value + "Available Students (Possibliy Not Registered yet)" + Color.RESET.value);
        System.out.println(Color.YELLOW.value +
                RegistrarDriver.students.values().stream().map(s -> s.getReport())
                        .reduce((s1, s2) -> s1 + "\n--------------------------------------\n" + s2)
                        .orElse("No Students Records\n")
                + Color.RESET.value);
    };

    private static Runnable action9 = () -> {
        System.out
                .println(Color.YELLOW.value + "Available Teachers (Possibliy Not Registered yet)" + Color.RESET.value);
        System.out.println(Color.YELLOW.value +
                RegistrarDriver.teachers.values().stream().map(t -> t.toString())
                        .reduce((t1, t2) -> t1 + "\n--------------------------------------\n" + t2)
                        .orElse("No Teachers Records\n")
                + Color.RESET.value);
    };

    private static Runnable action10 = () -> {
        System.out.println(Color.YELLOW.value + "Available Courses (Possibliy Not Registered yet)" + Color.RESET.value);
        System.out.println(Color.YELLOW.value +
                RegistrarDriver.courses.values().stream().map(c -> c.toString())
                        .reduce((c1, c2) -> c1 + "\n-----------------------------------\n" + c2)
                        .orElse("No Courses Available\n")
                + Color.RESET.value);
    };

    private static Runnable action12 = () -> {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(Color.YELLOW.value + "Enter student ID to enter the grade" + Color.RESET.value);
        String id = null;

        while (id == null) {
            try {
                id = br.readLine();
                Student stu = RegistrarDriver.students.get(Integer.parseInt(id));
                Set<Course> rc = new HashSet<>(stu.getRegisteredCourses());
                rc.stream().forEach(e -> {
                    System.out.println(
                            Color.YELLOW.value + "Enter The grade " + e.getCourseID() + ":" + Color.RESET.value);
                    try {
                        String grade = br.readLine();
                        stu.enterCourseGrade(e, grade);
                    } catch (Exception ex) {
                    }
                });

            } catch (NumberFormatException n) {
                System.out
                        .println(Color.YELLOW.value + "Invalid input. Please enter a String Grade" + Color.RESET.value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    private static Runnable action13 = () -> {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(Color.YELLOW.value + "Enter student ID to calculate GPA for: " + Color.RESET.value);
        String id = null;
        while (id == null) {
            try {
                id = br.readLine();
                Student stu = RegistrarDriver.students.get(Integer.parseInt(id));
                System.out.println(Color.YELLOW.value + "GPA for " + stu.getId() + " = " + stu.calculateGPA()
                        + "\nStudent Status: " + stu.getGpaStatus() + Color.RESET.value);
            } catch (NumberFormatException e) {
                System.out.println(Color.YELLOW.value + "Invalid input. Please enter a number" + Color.RESET.value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };

    private static Runnable action7 = () -> {
        System.out.println(Color.YELLOW.value + "Register Students and a Teacher in a Course" + Color.RESET.value);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(Color.YELLOW.value +
                "Enter Semester Name: \n(Or Enter 'show' to view available semesters, any key to create a new semester)"
                + Color.RESET.value);
        String semesterName = null;
        while (semesterName == null) {
            try {
                semesterName = br.readLine();
                if (semesterName.trim().toLowerCase().contains("show")) {
                    System.out.println(Color.YELLOW.value + "Available Semesters: " + Color.RESET.value);
                    System.out.println(Color.YELLOW.value +
                            RegistrarDriver.semesters.values().stream().map(s -> s.getSemesterName())
                                    .reduce((s1, s2) -> s1 + "\n-----------------------------------\n" + s2)
                                    .orElse("No Semesters Available\n")
                            + Color.RESET.value);
                    System.out.println(Color.YELLOW.value +
                            "Enter Semester Name: \n(Or Enter 'show' to view available semesters, any key to create a new semester)"
                            + Color.RESET.value);
                    semesterName = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String sName = semesterName;
        Semester semester = RegistrarDriver.semesters.values().stream()
                .filter(s -> s.getSemesterName().trim().toLowerCase().equals(sName.trim().toLowerCase()))
                .findAny().orElseGet(TaskSwitcher::createSemester);

        System.out.println(Color.YELLOW.value + "Enter Course Code: (Enter 'show' to view available courses) "
                + Color.RESET.value);
        String courseID = null;
        while (courseID == null) {
            try {
                courseID = br.readLine();
                if (courseID.trim().toLowerCase().equals("show")) {
                    action10.run();
                    System.out.println(Color.YELLOW.value
                            + "Enter Course Code: (Enter 'show' to view available courses) " + Color.RESET.value);
                    courseID = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Course course = Optional.ofNullable(RegistrarDriver.courses.get(courseID)).orElseGet(() -> {
            System.out.println(Color.YELLOW.value + "Course not found." + Color.RESET.value);
            return createCourse();
        });

        System.out.println(Color.YELLOW.value + "Enter Teacher ID: (Enter 'show' to view available Teachers) "
                + Color.RESET.value);
        int teacherID = 0;
        while (teacherID == 0) {
            try {
                String ans = br.readLine();
                if (ans.equals("show")) {
                    action9.run();
                    System.out.println(Color.YELLOW.value
                            + "Enter Teacher ID: (Enter 'show' to view available Teachers) " + Color.RESET.value);
                    teacherID = 0;
                } else {
                    teacherID = Integer.parseInt(ans);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println(Color.YELLOW.value + "Invalid input. Please enter a number" + Color.RESET.value);
            }
        }

        Teacher teacher = createTeacher(teacherID);

        List<Student> students = new ArrayList<>();

        int studentID = 0;
        while (studentID == 0) {
            try {
                System.out.println(Color.YELLOW.value
                        + "Enter Student ID: (Enter 'show' to view available Students or 'done' to finish)"
                        + Color.RESET.value);
                String ans = br.readLine();
                if (ans.equals("done")) {
                    break;
                } else if (ans.equals("show")) {
                    action8.run();
                    studentID = 0;
                } else {
                    studentID = Integer.parseInt(ans);
                    Student student = createStudent(studentID);
                    System.out.println(
                            Color.YELLOW.value + "Student " + student + " added successfully" + Color.RESET.value);
                    students.add(student);
                    studentID = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println(Color.YELLOW.value + "Invalid input. Please enter a number" + Color.RESET.value);
            }
        }

        System.out.println(Color.YELLOW.value + "Registered Students: \n\n" + students
                + "\n\n\nWith Teacher: \n\n" + teacher
                + "\n\n\nIn Course: \n\n" + course + Color.RESET.value);
        semester.registerInACourse(course, students, teacher);

    };

    private static Runnable action5 = () -> {
        System.out.println(Color.YELLOW.value + "add a prerequisite to a course" + Color.RESET.value);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(Color.YELLOW.value + "Enter Course Code: " + Color.RESET.value);
        String courseID = null;
        try {
            courseID = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Optional<Course> course = Optional.ofNullable(RegistrarDriver.courses.get(courseID));
        if (course.isPresent()) {
            System.out.println(Color.YELLOW.value + "Enter Prerequisite Course Code: " + Color.RESET.value);
            String prerequisiteID = null;
            try {
                prerequisiteID = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Course prerequisite = Optional.ofNullable(RegistrarDriver.courses.get(prerequisiteID))
                    .orElseGet(() -> {
                        System.out.println(Color.YELLOW.value + "Course not found." + Color.RESET.value);
                        return createCourse();
                    });

            course.get().addPrerequisites(prerequisite);
            System.out.println(
                    Color.YELLOW.value + "\nPREREQUISITE:\n\n" + prerequisite + "\n\nADDED SUCCESSFULLY TO COURSE:\n\n"
                            + course.get() + "\n" + Color.RESET.value);

        } else {
            System.out.println(Color.YELLOW.value + "Course not found" + Color.RESET.value);
        }
    };

    private static Runnable action11 = () -> {
        System.out.println(Color.YELLOW.value + "View all Semester Deatials" + Color.RESET.value);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(Color.YELLOW.value +
                "Enter Semester Name: \n(Or Enter 'show' to view available semesters)" + Color.RESET.value);
        String semesterName = null;
        while (semesterName == null) {
            try {
                semesterName = br.readLine();
                if (semesterName.trim().toLowerCase().contains("show")) {
                    System.out.println(Color.YELLOW.value + "Available Semesters: " + Color.RESET.value);
                    System.out.println(Color.YELLOW.value +
                            RegistrarDriver.semesters.values().stream().map(s -> s.getSemesterName())
                                    .reduce((s1, s2) -> s1 + "\n-----------------------------------\n" + s2)
                                    .orElse("No Semesters Available\n")
                            + Color.RESET.value);
                    System.out.println(Color.YELLOW.value +
                            "Enter Semester Name: \n(Or Enter 'show' to view available semesters)" + Color.RESET.value);
                    semesterName = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String sName = semesterName;
        Optional<Semester> semesteropt = RegistrarDriver.semesters.values().stream()
                .filter(s -> s.getSemesterName().trim().toLowerCase().equals(sName.trim().toLowerCase()))
                .findAny();
        if (!semesteropt.isPresent()) {

            System.out.println(Color.YELLOW.value + "Semester not found" + Color.RESET.value);
        } else {
            Semester semester = semesteropt.get();

            System.out.println(Color.YELLOW.value + semester + Color.RESET.value);

            System.out.println(Color.YELLOW.value + "Details are as follows: " + Color.RESET.value);
            System.out.println(Color.YELLOW.value + "Registered Courses: " + Color.RESET.value);
            semester.getRegisteredCourses().stream()
                    .forEach((c1) -> System.out
                            .println(Color.YELLOW.value + c1 + "\n" + c1.getTeacher() + "\n" + c1.getEnrolledStudents()
                                    + "\n-----------------------------------\n" + Color.RESET.value));
        }

    };

    /**
     * A switcher that returns a task to be executed on another thread by the
     * Regiatrar.
     * Action 1: Create New Semester
     * Action 2: Add New Student
     * Action 3: Add New Teacher
     * Action 4: Add New Course
     * Action 5: add prerequisite to a course
     * Action 6: View Course Prerequisites
     * Action 7: Register
     * Action 8: View Registered Students
     * Action 9: View Registered Teachers
     * Action 10: View Available Courses
     * Action 11: show all details of a semester
     * Action 12: Enter Student Grade
     * Action 13: Calculate student GPA
     * Action 0: Exit
     * 
     * @param input the action code to be executed
     * @return Task To be executed on another thread by the Regiatrar
     *
     */
    public static Task get(int input) {
        switch (input) {
            case 1:
                return new Task(input, action1);

            case 2:
                return new Task(input, action2);

            case 3:
                return new Task(input, action3);

            case 4:
                return new Task(input, action4);

            case 5:
                return new Task(input, action5);

            case 6:
                return new Task(input, action6);

            case 7:
                return new Task(input, action7);

            case 8:
                return new Task(input, action8);
            case 9:
                return new Task(input, action9);
            case 10:
                return new Task(input, action10);
            case 11:
                return new Task(input, action11);
            case 12:
                return new Task(input, action12);
            case 13:
                return new Task(input, action13);

            default:
                return new Task(input);
        }
    }
}