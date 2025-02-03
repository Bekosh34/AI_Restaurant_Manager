package com.ai.restaurant.database;

import com.ai.restaurant.model.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_PATH = "jdbc:sqlite:src/main/resources/restaurant.db";

    /**
     * Establishes a connection to the SQLite database.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_PATH);
    }

    /**
     * Creates the necessary tables if they do not exist.
     */
    public static void createTablesIfNotExists() {
        System.out.println("📂 Using Database Path: " + DB_PATH);

        String createPredictionsTable = """
        CREATE TABLE IF NOT EXISTS ai_predictions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            category TEXT NOT NULL,
            prediction TEXT NOT NULL,
            recommendation TEXT NOT NULL,
            timestamp TEXT DEFAULT CURRENT_TIMESTAMP,
            orders REAL,
            reservations REAL,
            inventory REAL
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
            stmt.execute(createPredictionsTable);
            stmt.execute(createReservationsTable);
            System.out.println("✅ Tables verified or created.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Failed to create tables.");
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
            e.printStackTrace();
            System.out.println("❌ Failed to add reservation.");
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
            pstmt.executeUpdate();
            System.out.println("✅ Reservation deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Failed to delete reservation.");
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
            e.printStackTrace();
            System.out.println("❌ Failed to load reservations.");
        }
        return reservations;
    }
}
