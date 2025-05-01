package studentdb.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import studentdb.model.Student;
import studentdb.util.DBUtil;


public class StudentDAO {

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (name, age, email, course, status,faculty_id) VALUES (?, ?, ?, ?, ?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getAge());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getCourse());
            stmt.setString(5, student.getStatus());
            stmt.setInt(6,student.getFacultyId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getString("status"),
                        rs.getInt("faculty_id")
                );
            }

        } catch (SQLException e) {
        }
        return null;
    }

    public List<Student> getStudentsByFacultyId(int facultyId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE faculty_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, facultyId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getString("status"),
                        rs.getInt("faculty_id")
                    );
                    students.add(student);
                }
            }
        } catch (SQLException e) {
          return null;
        }
        return students;
    }
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getString("status"),
                        rs.getInt("faculty_id")
                );
                students.add(student);
            }

        } catch (SQLException e) {
        }
        return students;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name=?, age=?, email=?, course=?, status=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getAge());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getCourse());
            stmt.setString(5, student.getStatus());
            stmt.setInt(6, student.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean importStudentsFromCSV(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        reader.readLine(); // skip header

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", -1);
            if (parts.length != 6) continue;

            String name = parts[0].trim();
            int age = Integer.parseInt(parts[1].trim());
            String email = parts[2].trim();
            String course = parts[3].trim();
            String status = parts[4].trim();
            int facultyId = Integer.parseInt(parts[5].trim());

            Student student = new Student(0, name, age, email, course, status, facultyId);
            addStudent(student);
        }
        return true;
    } catch (Exception e) {
        return false;
    }
}



    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public Student getStudentByName(String name) {
        String sql = "SELECT * FROM students WHERE name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getString("status"),
                        rs.getInt("faculty_id")
                );
            }

        } catch (SQLException e) {
        }
        return null;
    }

}
