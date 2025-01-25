package com.ai.restaurant.gui;

import com.ai.restaurant.database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsController {

    @FXML
    private TextArea reportArea;

    @FXML
    private void handleReservationReport() {
        String query = """
        SELECT COUNT(*) AS total_reservations,
               MAX(table_number) AS most_reserved_table
        FROM reservations
    """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String report = String.format("""
                Reservation Report:
                -------------------
                Total Reservations: %d
                Most Reserved Table: %d
                Peak Hours: 6:00 PM - 8:00 PM
            """, rs.getInt("total_reservations"),
                        rs.getInt("most_reserved_table"));

                reportArea.setText(report);
            }
        } catch (SQLException e) {
            reportArea.setText("Error generating reservation report: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleInventoryReport(ActionEvent event) {
        // Mock report for inventory
        String report = """
            Inventory Report:
            -----------------
            Total Items: 120
            Most Used Item: Cheese
            Low Stock: Tomatoes, Milk
        """;
        reportArea.setText(report);
    }

    @FXML
    private void handleStaffReport(ActionEvent event) {
        // Mock report for staff
        String report = """
            Staff Report:
            -------------
            Total Staff: 20
            Most Active Shift: Evening
            Vacancies: 2
        """;
        reportArea.setText(report);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        // Navigate back to the main menu or close the current window
        Stage stage = (Stage) reportArea.getScene().getWindow();
        stage.close(); // Close the current window
    }
}
