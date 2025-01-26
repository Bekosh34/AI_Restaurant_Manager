package com.ai.restaurant.managers;

import com.ai.restaurant.database.DatabaseManager;
import com.ai.restaurant.model.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffManager {

    public static boolean addStaff(Staff staff) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO staff (name, role) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, staff.getName());
            statement.setString(2, staff.getRole());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM staff";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Staff staff = new Staff(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("role")
                );
                staffList.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public static boolean updateStaffRole(int staffId, String newRole) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "UPDATE staff SET role = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newRole);
            statement.setInt(2, staffId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteStaff(int staffId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM staff WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, staffId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
