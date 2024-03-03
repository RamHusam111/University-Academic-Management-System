package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents the registrar of the university.
 */
public class RegistrarDriver {

    public static Map<String, Semester> semesters = new LinkedHashMap<>();
    public static Map<Integer, Student> students = new LinkedHashMap<>();
    public static Map<Integer, Teacher> teachers = new LinkedHashMap<>();
    public static Map<String, Course> courses = new LinkedHashMap<>();
    public static List<WeeklyMeeting> weeklyMeetings = new ArrayList<>();
    public static Map<String, Specialization> specializations = new LinkedHashMap<>();
    public static Map<String, Faculty> faculties = new LinkedHashMap<>();

    public static void main(String[] args) throws InterruptedException, IOException {
        readFiles();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int input = 1;
        System.out.println(Color.RED.value + ">>>>>>>>>>>>> Welcome to the University Registrar <<<<<<<<<<<<<<<\n"
                + Color.RESET.value);
        while (input != 0) {
            System.out.println(Color.RED.value + ">>>>>>>>>>>> Choose what you want to do: " + Color.RESET.value);
            System.out.println(
                    Color.PURPLE.value + "\n1 - Create New Semester"
                            + Color.PURPLE.value + "\n2  - Add New Student"
                            + Color.PURPLE.value + "\n3  - Add New Teacher"
                            + Color.PURPLE.value + "\n4  - Add New Course"
                            + Color.PURPLE.value + "\n5  - Add Prerequisite to a Course"
                            + Color.PURPLE.value + "\n6  - View Course Prerequisites"
                            + Color.PURPLE.value + "\n7  - Register"
                            + Color.PURPLE.value + "\n8  - View Registered Students"
                            + Color.PURPLE.value + "\n9  - View Registered Teachers"
                            + Color.PURPLE.value + "\n10 - View Available Courses"
                            + Color.PURPLE.value + "\n11 - Show All Details of a Semester"
                            + Color.PURPLE.value + "\n12 - Enter Student Grade"
                            + Color.PURPLE.value + "\n13 - Calculate Student GPA"
                            + Color.PURPLE.value + "\n0  - Exit"
                            + Color.RESET.value);

            try {
                input = Integer.parseInt(in.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 0 and 13");
                continue;
            }

            Task t = TaskSwitcher.get(input);

            t.start();
            t.join();

        }

        System.out.println(Color.RED.value + ">>>>>>>>>>>>> Goodbye! <<<<<<<<<<<<<<<" + Color.RESET.value);

        in.close();

    }

    /**
     * Reads the CSV files and populates the data structures.
     */
    public static void readFiles() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/WeeklyMeetings.csv"));
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] values = line.split(",");
                DayOfWeek day = DayOfWeek.valueOf(values[0].toUpperCase());
                Duration duration = Duration.ofMinutes(Long.parseLong(values[1]));
                String room = values[2];
                LocalTime hour = LocalTime.parse(values[3]);
                WeeklyMeeting wm = new WeeklyMeeting(day, duration, room, hour);
                weeklyMeetings.add(wm);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/Faculties.csv"));
            line = bufferedReader.readLine();
            while (line != null) {
                Faculty faculty = new Faculty(line);
                faculties.put(faculty.getName(), faculty);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/SemesterDates.csv"));
            line = bufferedReader.readLine();
            while (line != null) {
                String[] values = line.split(",");
                Semester semester = new Semester(LocalDate.parse(values[0].trim()), LocalDate.parse(values[1].trim()));
                semesters.put(semester.getSemesterName(), semester);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/students.csv"));
            line = bufferedReader.readLine();
            while (line != null) {
                String[] values = line.split(",");
                Specialization spec = new Specialization(values[1], faculties.get(values[2]),
                        Specialization.Type.valueOf(values[3]));
                specializations.put(spec.getName(), spec);
                Student student = new Student(values[0], spec);
                students.put(student.getId(), student);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/Teachers.csv"));
            line = bufferedReader.readLine();
            while (line != null) {
                String[] values = line.split(",");
                Teacher teacher = new Teacher(values[0], specializations.get(values[1]));
                teachers.put(teacher.getId(), teacher);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/Courses.csv"));
            line = bufferedReader.readLine();
            int courseCount = 0;
            while (line != null) {
                String[] values = line.split(",");
                int startIndex = courseCount * 3;
                List<WeeklyMeeting> courseMeetings = IntStream.range(startIndex, startIndex + 3)
                        .mapToObj(index -> weeklyMeetings.get(index))
                        .collect(Collectors.toList());
                Course course = null;
                if (faculties.containsKey(values[2])) {
                    course = new Course(values[0], values[1], faculties.get(values[2]),
                            Integer.parseInt(values[3]), courseMeetings, Integer.parseInt(values[4]));
                } else {
                    course = new Course(values[0], values[1], specializations.get(values[2]),
                            Integer.parseInt(values[3]), courseMeetings, Integer.parseInt(values[4]));
                }
                courses.put(course.getCourseID(), course);
                line = bufferedReader.readLine();
                courseCount++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
