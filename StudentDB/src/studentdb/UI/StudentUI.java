package studentdb.UI;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import studentdb.dao.*;
import studentdb.model.Student;

public class StudentUI extends JFrame {
    private final StudentDAO studentDAO = new StudentDAO();
    private final FacultyDAO facultyDAO = new FacultyDAO();
    public StudentUI() {
        setTitle("StudentDB - Dashboard");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 5));

        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View All Students");
        JButton importButton = new JButton("Import from CSV");
        JButton exitButton = new JButton("Exit");
        JButton updateDetailsButton = new JButton("Update Details");
        JButton viewByFacultyButton = new JButton("View Students by Faculty");
        JButton deleteButton = new JButton("Delete Student");
    
        
        add(addButton);
        add(viewButton);
        add(importButton);
        add(updateDetailsButton);
        add(viewByFacultyButton);
        add(exitButton);
        
        // Add Student Handler
        addButton.addActionListener(e -> addStudent());

        // View All Students
        viewButton.addActionListener(e -> viewAllStudents());

        // Import from CSV
        importButton.addActionListener(e -> importFromCSV());

        //update details button
        updateDetailsButton.addActionListener(e -> updateStudentDetails());
        // Exit
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void updateStudentDetails() {
        String input = JOptionPane.showInputDialog("Enter Student ID to update:");
        if (input == null || input.isEmpty()) return;
    
        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.");
            return;
        }
    
        Student student = studentDAO.getStudentById(id);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found.");
            return;
        }
    
        JFrame updateFrame = new JFrame("Update Student: " + student.getName());
        updateFrame.setSize(400, 300);
        updateFrame.setLayout(new GridLayout(7, 1, 10, 10));
        updateFrame.setLocationRelativeTo(null);
    
        JButton nameButton = new JButton("Update Name");
        JButton ageButton = new JButton("Update Age");
        JButton emailButton = new JButton("Update Email");
        JButton courseButton = new JButton("Update Course");
        JButton statusButton = new JButton("Update Status");
        JButton facultyIdButton = new JButton("Update Faculty ID");
        JButton closeButton = new JButton("Done");
    
        updateFrame.add(nameButton);
        updateFrame.add(ageButton);
        updateFrame.add(emailButton);
        updateFrame.add(courseButton);
        updateFrame.add(statusButton);
        updateFrame.add(facultyIdButton);
        updateFrame.add(closeButton);
    
        nameButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter new name:", student.getName());
            if (newName != null && !newName.isEmpty()) {
                student.setName(newName);
                studentDAO.updateStudent(student);
            }
        });
    
        ageButton.addActionListener(e -> {
            String ageInput = JOptionPane.showInputDialog("Enter new age:", student.getAge());
            try {
                int newAge = Integer.parseInt(ageInput);
                student.setAge(newAge);
                studentDAO.updateStudent(student);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(updateFrame, "Invalid age.");
            }
        });
    
        emailButton.addActionListener(e -> {
            String newEmail = JOptionPane.showInputDialog("Enter new email:", student.getEmail());
            if (newEmail != null && !newEmail.isEmpty()) {
                student.setEmail(newEmail);
                studentDAO.updateStudent(student);
            }
        });
    
        courseButton.addActionListener(e -> {
            String newCourse = JOptionPane.showInputDialog("Enter new course:", student.getCourse());
            if (newCourse != null && !newCourse.isEmpty()) {
                student.setCourse(newCourse);
                studentDAO.updateStudent(student);
            }
        });
    
        statusButton.addActionListener(e -> {
            String newStatus = JOptionPane.showInputDialog("Enter new status:", student.getStatus());
            if (newStatus != null && !newStatus.isEmpty()) {
                student.setStatus(newStatus);
                studentDAO.updateStudent(student);
            }
        });
    
        facultyIdButton.addActionListener(e -> {
            String newFacultyIdStr = JOptionPane.showInputDialog("Enter new Faculty ID:", student.getFacultyId());
            try {
                int newFacultyId = Integer.parseInt(newFacultyIdStr);
                student.setFacultyId(newFacultyId);
                studentDAO.updateStudent(student);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(updateFrame, "Invalid Faculty ID.");
            }
        });
    
        closeButton.addActionListener(e -> updateFrame.dispose());
    
        updateFrame.setVisible(true);
    }
    private void addStudent() {
        try {
            String name = JOptionPane.showInputDialog("Enter name:");
            int age = Integer.parseInt(JOptionPane.showInputDialog("Enter age:"));
            String email = JOptionPane.showInputDialog("Enter email:");
            String course = JOptionPane.showInputDialog("Enter course:");
            String status = JOptionPane.showInputDialog("Enter status (active/graduated/dropped):");
            int facultyId = Integer.parseInt(JOptionPane.showInputDialog("Enter faculty ID:"));

            Student s = new Student(0, name, age, email, course, status, facultyId);
            boolean success = studentDAO.addStudent(s);

            JOptionPane.showMessageDialog(this, success ? "Student added!" : "Failed to add student.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void viewAllStudents() {
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students found.");
            return;
        }

        StringBuilder sb = new StringBuilder("Students:\n");
        for (Student s : students) {
            sb.append(String.format("%d - %s (%s)\n", s.getId(), s.getName(), s.getEmail()));
        }
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, scrollPane, "Student List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void importFromCSV() {
        JButton importCsvButton = new JButton("Import CSV");
        JPanel csvOptionsPanel = new JPanel(new GridLayout(1, 2)); // Hidden by default

        JButton studentCsvBtn = new JButton("Student CSV");
        JButton facultyCsvBtn = new JButton("Faculty CSV");

        csvOptionsPanel.add(studentCsvBtn);
        csvOptionsPanel.add(facultyCsvBtn);
        csvOptionsPanel.setVisible(false); // Initially hidden

        importCsvButton.addActionListener(e -> {
            // Toggle visibility
            csvOptionsPanel.setVisible(!csvOptionsPanel.isVisible());
        });

// Handle actions
studentCsvBtn.addActionListener(e -> handleCsvUpload("student"));
facultyCsvBtn.addActionListener(e -> handleCsvUpload("faculty"));

    }
    private void handleCsvUpload(String type) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if ("student".equals(type)) {
                studentDAO.importStudentsFromCSV(path);
            } else {
                facultyDAO.importFacultyFromCSV(path);
            }
            JOptionPane.showMessageDialog(this, type + " CSV import complete.");
        }
    }
    
    private void importStudentsFromCSV() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            boolean success = studentDAO.importStudentsFromCSV(chooser.getSelectedFile().getAbsolutePath());
            JOptionPane.showMessageDialog(this, success ? "Import successful." : "Import failed.");
        }
    }
    private void importFacultiesFromCSV() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            boolean success = facultyDAO.importFacultyFromCSV(chooser.getSelectedFile().getAbsolutePath());
            JOptionPane.showMessageDialog(this, success ? "Import successful." : "Import failed.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentUI().setVisible(true));
    }
}
