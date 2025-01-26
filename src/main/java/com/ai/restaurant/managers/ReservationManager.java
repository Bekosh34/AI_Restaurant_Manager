package com.ai.restaurant.managers;

import com.ai.restaurant.database.DatabaseManager;
import com.ai.restaurant.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {

    public static boolean addReservation(Reservation reservation) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO reservations (customer_name, date, table_number) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reservation.getCustomerName());
            statement.setString(2, reservation.getDate());
            statement.setInt(3, reservation.getTableNumber());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM reservations";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("date"),
                        resultSet.getInt("table_number")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}
