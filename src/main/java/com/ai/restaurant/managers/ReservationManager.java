package com.ai.restaurant.managers;

import com.ai.restaurant.database.DatabaseManager;
import com.ai.restaurant.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ai.restaurant.database.DatabaseHelper;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class ReservationManager {

    public static List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM reservations";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                reservations.add(new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("date"),
                        resultSet.getInt("table_number") // Ensure this matches the database type
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public static Map<String, Integer> getReservationCountsByDate() {
        Map<String, Integer> reservationCounts = new HashMap<>();
        String query = "SELECT date, COUNT(*) FROM reservations GROUP BY date";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reservationCounts.put(rs.getString("date"), rs.getInt("COUNT(*)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationCounts;
    }


    public static boolean addReservation(Reservation reservation) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO reservations (customer_name, date, table_number) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reservation.getCustomerName());
            statement.setString(2, reservation.getDate());
            statement.setInt(3, reservation.getTableNumber()); // Pass tableNumber as int
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteReservation(int id) {
        // Logic to delete a reservation by ID from the database
        // Example:
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM reservations WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
