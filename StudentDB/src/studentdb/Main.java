package studentdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import studentdb.dao.FacultyDAO;
import studentdb.dao.StudentDAO;
import studentdb.model.Faculty;
import studentdb.model.Student;
import studentdb.util.DBUtil;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();

    private static final FacultyDAO facultyDAO = new FacultyDAO();

    public static void main(String[] args) {
        int choice;
        boolean running = true;
        do {
            printMenu();
            choice = Integer.parseInt(scanner.nextLine());
        
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> listStudents();
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> addFaculty();
                case 6 -> viewAllFaculty();
                case 7 -> viewStudentsByFaculty();
                case 8 -> importStudentsFromCSV();
                case 9 -> running = false;

                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (running );
    }

    private static void printMenu() {
        System.out.println("\n=== StudentDB Menu ===");
        System.out.println("1. Add Student");
        System.out.println("2. List All Students");
        System.out.println("3. Update Student");
        System.out.println("4. Delete Student");

        System.out.println("5. Add Faculty");
        System.out.println("6. View All Faculty");
        System.out.println("7 View students by faculty");
        System.out.println("8. Import students from CSV");


        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("^[a-zA-Z\\s]+$");
    }
    public static boolean isValidAge(int age) {
        return age >= 18 && age <= 100;
    }
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
    public static boolean isValidCourse(String course) {
        return course != null && !course.trim().isEmpty() && course.matches("^[a-zA-Z\\s]+$");
    }
    public static boolean isValidStatus(String status) {
        return status != null && (status.equals("active") || status.equals("graduated") || status.equals("dropped"));
    }
    public static boolean isValidFacultyId(int id) {
    String sql = "SELECT id FROM faculty WHERE id = ?";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next(); // returns true if faculty ID exists
        }
    } catch (SQLException e) {
        return false;
    }
}

    public static boolean isValidStudent(String name, int age, String email, String course, String status) {
        return isValidName(name) && isValidAge(age) && isValidEmail(email) && isValidCourse(course) && isValidStatus(status);
    }
    
    //1.Adding student details
    private static void addStudent() {
        String name, email, course, status;
        int age,faculty_id;
    
        // Name
        while (true) {
            System.out.print("Name: ");
            name = scanner.nextLine();
            if (isValidName(name)) break;
            System.out.println("Invalid name. Only letters and spaces allowed.");
        }
    
        // Age
        while (true) {
            System.out.print("Age: ");
            try {
                age = Integer.parseInt(scanner.nextLine());
                if (isValidAge(age)) break;
                else System.out.println("Invalid age. Must be between 18 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    
        // Email
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) break;
            System.out.println("Invalid email format.");
        }
    
        // Course
        while (true) {
            System.out.print("Course: ");
            course = scanner.nextLine();
            if (isValidCourse(course)) break;
            System.out.println("Invalid course name. Only letters and spaces allowed.");
        }
    
        // Status
        while (true) {
            System.out.print("Status (active/graduated/dropped): ");
            status = scanner.nextLine().toLowerCase(); // make it case-insensitive
            if (isValidStatus(status)) break;
            System.out.println("Invalid status. Must be 'active', 'graduated', or 'dropped'.");
        }
        
        // facultyid
        while (true) { 
            System.out.print("Faculty ID (or 0 for no mentor): ");
            faculty_id = Integer.parseInt(scanner.nextLine());
            if(isValidFacultyId(faculty_id)|| faculty_id == 0) break;
            System.out.println("Invalid faculty ID."); 
        }
        // Create and insert the student
        boolean success = studentDAO.addStudent( new Student(0, name, age, email, course, status,faculty_id));
        System.out.println(success ? "✅ Student added successfully!" : "❌ Failed to add student.");
    }
        
    //2.Print all the students
    private static void listStudents() {
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    //3.Update student
    private static void updateStudent() {
        System.out.print("Enter ID of student to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        Student existing = studentDAO.getStudentById(id);
        if (existing == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("New Name (" + existing.getName() + "): ");
        String name = scanner.nextLine();

        System.out.print("New Age (" + existing.getAge() + "): ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("New Email (" + existing.getEmail() + "): ");
        String email = scanner.nextLine();

        System.out.print("New Course (" + existing.getCourse() + "): ");
        String course = scanner.nextLine();

        System.out.print("New Status (" + existing.getStatus() + "): ");
        String status = scanner.nextLine();

        System.out.print("New facultyId (" + existing.getFacultyId() + "): ");
        int facultyId = Integer.parseInt(scanner.nextLine());

        Student updated = new Student(id, name, age, email, course, status,facultyId);
        boolean success = studentDAO.updateStudent(updated);
        System.out.println(success ? "Student updated." : "Update failed.");
    }
    
    //4.Delete studentq
    private static void deleteStudent() {
        System.out.print("Enter ID of student to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        boolean success = studentDAO.deleteStudent(id);
        System.out.println(success ? "Student deleted." : "Delete failed.");
    }
    
    //5.Adding faculty
    private static void addFaculty() {
        String name, email, dept;
        while (true) {
            System.out.print("Name: ");
            name = scanner.nextLine();
            if (isValidName(name)) break;
            System.out.println("Invalid name. Only letters and spaces allowed.");        
        }
        while (true) {
            System.out.print("Department: ");
            dept = scanner.nextLine();
            if (isValidName(dept)) break;
            System.out.println("Invalid department. Only letters and spaces allowed.");                
        }
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) break;
            System.out.println("Invalid email format.");        
        }
        
        Faculty faculty = new Faculty(0, name, dept, email);
        boolean success = facultyDAO.addFaculty(faculty);
        System.out.println(success ? "Faculty added." : "Failed to add faculty.");
    }
    
    //6. View All Faculty
    private static void viewAllFaculty() {
        List<Faculty> facultyList = facultyDAO.getAllFaculty();
        if (facultyList.isEmpty()) {
            System.out.println("No faculty records found.");
        } else {
            for (Faculty f : facultyList) {
                System.out.println(f.getId() + " | " + f.getName() + " | " +
                                   f.getDepartment() + " | " + f.getEmail());
            }
        }
    }
    
    //7. View Students by Faculty
    private static void viewStudentsByFaculty() {
        System.out.print("Enter Faculty ID: ");
        int facultyId = Integer.parseInt(scanner.nextLine());
    
        if (!isValidFacultyId(facultyId)) {
            System.out.println("Invalid faculty ID.");
            return;
        }
    
        List<Student> students = studentDAO.getStudentsByFacultyId(facultyId);
        if (students == null || students.isEmpty()) {
            System.out.println("No students found for this faculty.");
        } else {
            System.out.println("Students mentored by Faculty ID " + facultyId + ":");
            for (Student s : students) {
                System.out.printf("%d | %s | %d | %s | %s | %s%n",
                    s.getId(), s.getName(), s.getAge(),
                    s.getEmail(), s.getCourse(), s.getStatus());
            }
        }
    }
    
    //8. Import students from CSV
    private static void importStudentsFromCSV() {
        System.out.print("Enter CSV file path: ");
        String csvFilePath = scanner.nextLine();
        boolean success = studentDAO.importStudentsFromCSV(csvFilePath);
        System.out.println(success ? "Students imported successfully." : "Failed to import students.");
    }
}
