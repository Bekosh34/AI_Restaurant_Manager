package com.ai.restaurant.managers;

import com.ai.restaurant.database.DatabaseConnection;
import com.ai.restaurant.database.DatabaseManager;
import com.ai.restaurant.model.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffManager {
    public static List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * FROM staff";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                staffList.add(new Staff(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Staff retrieved: " + staffList.size());
        return staffList;
    }

    public static void addStaff(Staff staff) {
        String query = "INSERT INTO staff (name, role) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStaff(int id) {
        String query = "DELETE FROM staff WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateStaff(Staff staff) {
        String query = "UPDATE staff SET role = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, staff.getRole());
            stmt.setInt(2, staff.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
