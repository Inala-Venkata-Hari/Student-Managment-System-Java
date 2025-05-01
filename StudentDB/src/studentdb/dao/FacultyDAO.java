package studentdb.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import studentdb.model.Faculty;
import studentdb.util.DBUtil;

public class FacultyDAO {

    public boolean addFaculty(Faculty faculty) {
        String sql = "INSERT INTO faculty (name, department, email) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, faculty.getName());
            stmt.setString(2, faculty.getDepartment());
            stmt.setString(3, faculty.getEmail());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding faculty: " + e.getMessage());
            return false;
        }
    }

    public List<Faculty> getAllFaculty() {
        List<Faculty> list = new ArrayList<>();
        String sql = "SELECT * FROM faculty";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Faculty f = new Faculty(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("email")
                );
                list.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching faculty: " + e.getMessage());
        }

        return list;
    }

    public Faculty getFacultyById(int id) {
        String sql = "SELECT * FROM faculty WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Faculty(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("email")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching faculty by ID: " + e.getMessage());
        }

        return null;
    }

    public boolean updateFaculty(Faculty faculty) {
        String sql = "UPDATE faculty SET name = ?, department = ?, email = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, faculty.getName());
            stmt.setString(2, faculty.getDepartment());
            stmt.setString(3, faculty.getEmail());
            stmt.setInt(4, faculty.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating faculty: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFaculty(int id) {
        String sql = "DELETE FROM faculty WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting faculty: " + e.getMessage());
            return false;
        }
    }
    public boolean importFacultyFromCSV(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        reader.readLine(); // skip header

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", -1);
            if (parts.length != 3) continue;

            String name = parts[0].trim();
            String department = parts[1].trim();
            String email = parts[2].trim();
           
            Faculty faculty = new Faculty(0,name,department,email);
            addFaculty(faculty);
        }
        return true;
    } catch (Exception e) {
        return false;
    }
}
}
