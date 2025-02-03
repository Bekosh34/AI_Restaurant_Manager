package com.ai.restaurant.database;

import com.ai.restaurant.model.Reservation;
import com.ai.restaurant.model.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_PATH = "jdbc:sqlite:restaurant.db"; // Fixed relative path

    /**
     * Establishes a connection to the SQLite database.
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_PATH);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    /**
     * Creates the necessary tables if they do not exist.
     */
    public static void createTablesIfNotExists() {
        System.out.println("📂 Using Database Path: " + DB_PATH);

        String createStaffTable = """
        CREATE TABLE IF NOT EXISTS staff (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            role TEXT NOT NULL
        );
        """;

        String createReservationsTable = """
        CREATE TABLE IF NOT EXISTS reservations (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            customer_name TEXT NOT NULL,
            date TEXT NOT NULL,
            table_number INTEGER NOT NULL
        );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createStaffTable);
            stmt.execute(createReservationsTable);
            System.out.println("✅ Tables verified or created.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to create tables: " + e.getMessage());
        }
    }

    /**
     * Adds a new reservation to the database.
     */
    public static void addReservation(String customerName, String date, int tableNumber) {
        String query = "INSERT INTO reservations (customer_name, date, table_number) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, date);
            pstmt.setInt(3, tableNumber);
            pstmt.executeUpdate();
            System.out.println("✅ Reservation added successfully.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to add reservation: " + e.getMessage());
        }
    }

    /**
     * Deletes a reservation from the database.
     */
    public static void deleteReservation(int id) {
        String query = "DELETE FROM reservations WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Reservation deleted successfully.");
            } else {
                System.out.println("⚠ No reservation found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to delete reservation: " + e.getMessage());
        }
    }

    /**
     * Retrieves all reservations from the database.
     */
    public static List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT id, customer_name, date, table_number FROM reservations";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("date"),
                        rs.getInt("table_number")
                ));
            }
            System.out.println("✅ Reservations loaded: " + reservations.size());
        } catch (SQLException e) {
            System.out.println("❌ Failed to load reservations: " + e.getMessage());
        }
        return reservations;
    }

    /**
     * Adds a new staff member to the database.
     */
    public static void addStaff(Staff staff) {
        String query = "INSERT INTO staff (name, role) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getRole());
            pstmt.executeUpdate();
            System.out.println("✅ Staff added successfully: " + staff.getName());
        } catch (SQLException e) {
            System.out.println("❌ Failed to add staff: " + e.getMessage());
        }
    }

    /**
     * Deletes a staff member from the database.
     */
    public static void deleteStaff(int id) {
        String query = "DELETE FROM staff WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Staff deleted successfully: ID " + id);
            } else {
                System.out.println("⚠️ No staff found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to delete staff: " + e.getMessage());
        }
    }

    /**
     * Updates a staff member's role in the database.
     */
    public static void updateStaff(Staff staff) {
        String query = "UPDATE staff SET role = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, staff.getRole());
            pstmt.setInt(2, staff.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Staff role updated: " + staff.getName() + " -> " + staff.getRole());
            } else {
                System.out.println("⚠️ No staff found with ID: " + staff.getId());
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to update staff: " + e.getMessage());
        }
    }

    /**
     * Retrieves all staff members from the database.
     */
    public static List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT id, name, role FROM staff";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Staff staff = new Staff(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role")
                );
                staffList.add(staff);
            }

            if (staffList.isEmpty()) {
                System.out.println("⚠️ No staff members found.");
            } else {
                System.out.println("✅ Staff loaded successfully:");
                for (Staff staff : staffList) {
                    System.out.println("   - ID: " + staff.getId() + ", Name: " + staff.getName() + ", Role: " + staff.getRole());
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to load staff: " + e.getMessage());
        }
        return staffList;
    }
}
