package com.ai.restaurant.managers;

import com.ai.restaurant.model.Staff;
import com.ai.restaurant.database.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffManager {

    public static List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT id, name, role, salary FROM staff"; // Ensure salary is fetched

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String role = rs.getString("role");
                double salary = rs.getDouble("salary");

                staffList.add(new Staff(id, name, role, salary));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching staff data: " + e.getMessage());
        }
        return staffList;
    }

    public static boolean updateStaffRole(int staffId, String newRole) {
        String query = "UPDATE staff SET role = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newRole);
            pstmt.setInt(2, staffId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating staff role: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteStaff(int staffId) {
        String query = "DELETE FROM staff WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, staffId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting staff: " + e.getMessage());
            return false;
        }
    }


    public static boolean addStaff(Staff staff) {
        String query = "INSERT INTO staff (name, role, salary) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getRole());
            pstmt.setDouble(3, staff.getSalary());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding staff: " + e.getMessage());
            return false;
        }
    }
}
