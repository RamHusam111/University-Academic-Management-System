
### University Academic Management System

---

#### Project Title
**University Academic Management System**

#### Description
This project is a comprehensive academic management system implemented in Java (utilizing Java 8 features). It is designed to manage various aspects of a university environment, including course scheduling, student and faculty information, and academic records. The system facilitates the organization and tracking of academic activities, making it an essential tool for educational administrators and students alike.

#### Installation
1. Ensure you have Java installed on your system (Java 8 or above recommended).
2. Clone the repository to your local machine.
3. Open the project in your preferred Java IDE (e.g., Eclipse, IntelliJ IDEA).
4. Build the project to resolve any dependencies.

#### Usage
- To run the system, execute the `RegistrarDriver.java` file.
- This serves as the main entry point for the application and will guide you through the various functionalities.

#### Components and Functionalities
- **Person.java**: Base class for defining common attributes of individuals in the system (students, faculty, etc.).
- **Student.java**: Manages student-specific information, including enrollment, grades, and academic status.
- **Faculty.java**: Handles faculty details and their associations with courses and academic activities.
- **Course.java**: Represents individual courses, including course details, schedule, and enrolled students.
- **Semester.java**: Manages information related to academic semesters, such as duration and associated courses.
- **Specialization.java**: Defines various specializations or majors that students can enroll in.
- **Teacher.java**: A subclass of Faculty, specifically focused on teaching responsibilities and course management.
- **WeeklyMeeting.java**: Manages weekly meetings or classes, including time, location, and participants.
- **TaskSwitcher.java**: Potentially a utility class for managing tasks or operations within the system.
- **RegistrarDriver.java**: The main driver class that initiates and orchestrates the functionality of the entire system.
- **Task.java**: Manages concurrent tasks in the system, handling parallel processes with specific process IDs, useful for operations requiring multithreading.

#### Features
- **Student Management**: Keep track of student information, including courses enrolled, grades, and GPA.
- **Faculty Management**: Manage faculty details and their association with different courses.
- **Course Scheduling**: Automated scheduling of courses for each semester.
- **Academic Records**: Maintain detailed academic records for both students and faculty.
- **User-Friendly Interface**: Easy-to-navigate interface for managing and accessing information.

## Setup Instructions
1. Ensure Java is installed on your system.
2. Compile each Java file using a Java compiler (e.g., `javac ClassName.java`).
3. Run the `RegistrarDriver` class to start the application (e.g., `java RegistrarDriver`).


---
