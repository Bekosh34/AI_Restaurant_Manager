package com.ai.restaurant.managers;

import com.ai.restaurant.database.DatabaseConnection;
import com.ai.restaurant.database.DatabaseManager;
import com.ai.restaurant.model.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {
    public static List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";

        try (Connection conn = DatabaseManager.getConnection();
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

        System.out.println("Reservations retrieved: " + reservations.size());
        return reservations;
    }

    public static void addReservation(Reservation reservation) {
        String query = "INSERT INTO reservations (customer_name, date, table_number) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, reservation.getCustomerName());
            stmt.setString(2, reservation.getDate());
            stmt.setInt(3, reservation.getTableNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteReservation(int id) {
        String query = "DELETE FROM reservations WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
