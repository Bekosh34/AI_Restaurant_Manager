package com.ai.restaurant.managers;

import com.ai.restaurant.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReservationManager {
    public static void addReservation(String customerName, String date, int tableNumber) {
        Connection conn = DatabaseConnection.connect();
        String sql = "INSERT INTO reservations (customer_name, date, table_number) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, date);
            pstmt.setInt(3, tableNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
