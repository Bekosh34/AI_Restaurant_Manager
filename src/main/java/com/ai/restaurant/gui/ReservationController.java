package com.ai.restaurant.gui;

import com.ai.restaurant.ai.AIModel;
import com.ai.restaurant.database.DatabaseManager;
import com.ai.restaurant.model.Reservation;
import com.ai.restaurant.model.TrainModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import weka.core.Instances;

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

    @FXML
    public void initialize() {
        try {
            // Fetch historical data from the database
            Instances dataset = TrainModel.createEmptyDataset();
            Connection connection = DatabaseManager.getConnection();
            String sql = "SELECT feature1, feature2, target FROM reservations";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Populate the dataset
            while (resultSet.next()) {
                double[] features = {
                        resultSet.getDouble("feature1"),
                        resultSet.getDouble("feature2")
                };
                double target = resultSet.getDouble("target");
                TrainModel.addInstance(dataset, features, target);
            }

            // Train the AI model
            AIModel.trainModel(dataset);
            System.out.println("AI Model trained successfully!");
        } catch (Exception e) {
            System.err.println("Error training AI Model: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void updateAIRecommendations() {
        try {
            double[] currentFeatures = {12.0, 5.0}; // Example: current hour and table count
            String prediction = AIModel.predictReservation(currentFeatures);
            aiRecommendationLabel.setText("AI Suggestion: " + prediction);
        } catch (Exception e) {
            aiRecommendationLabel.setText("AI Error: Unable to generate suggestions.");
            e.printStackTrace();
        }
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
        try {
            double[] currentFeatures = {12.0, 5.0}; // Example: current hour and table count
            String prediction = AIModel.predictReservation(currentFeatures);
            aiRecommendationLabel.setText("AI Suggestion: " + prediction);
        } catch (Exception e) {
            aiRecommendationLabel.setText("AI Error: Unable to generate suggestions.");
            e.printStackTrace();
        }
    }
}

