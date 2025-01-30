package com.ai.restaurant.managers;

import com.ai.restaurant.database.DatabaseHelper;
import com.ai.restaurant.model.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {

    // Retrieve all reservations from the database
    public static List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";

        try (Connection conn = DatabaseHelper.connect();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Add a new reservation to the database
    public static boolean addReservation(Reservation reservation) {
        String query = "INSERT INTO reservations (customer_name, date, table_number) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, reservation.getCustomerName());
            pstmt.setString(2, reservation.getDate());
            pstmt.setInt(3, reservation.getTableNumber());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a reservation from the database by ID
    public static boolean deleteReservation(int id) {
        String query = "DELETE FROM reservations WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
