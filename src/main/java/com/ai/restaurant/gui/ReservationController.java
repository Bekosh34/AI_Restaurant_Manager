package com.ai.restaurant.gui;

import com.ai.restaurant.ai.AIModel;
import com.ai.restaurant.database.DatabaseManager;
import com.ai.restaurant.model.Reservation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationController {
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField tableNumberField;
    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private Label aiSuggestionLabel;

    public void initialize() {
        updateAIRecommendations();
    }

    @FXML
    private void handleAddReservation() {
        String customerName = customerNameField.getText();
        String date = dateField.getText();
        String tableNumber = tableNumberField.getText();

        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO reservations (customer_name, date, table_number) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customerName);
            statement.setString(2, date);
            statement.setString(3, tableNumber);
            statement.executeUpdate();

            updateAIRecommendations();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAIRecommendations() {
        double[] features = {12.0, 5.0}; // Example features (avg reservations, current time slot)
        String prediction = AIModel.predictReservation(features);
        aiSuggestionLabel.setText(prediction);
    }
}
