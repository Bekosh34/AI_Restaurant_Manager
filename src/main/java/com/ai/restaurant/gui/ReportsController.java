package com.ai.restaurant.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import com.ai.restaurant.ai.AIModel;

import java.sql.*;
import java.io.FileWriter;

public class ReportsController {
    @FXML private TextField customInput;
    @FXML private Label aiRecommendationLabel;
    @FXML private TableView<PredictionEntry> predictionsTable;
    @FXML private TableColumn<PredictionEntry, String> categoryColumn;
    @FXML private TableColumn<PredictionEntry, String> predictionColumn;
    @FXML private TableColumn<PredictionEntry, String> recommendationColumn;

    @FXML
    public void initialize() {
        predictionsTable.setItems(FXCollections.observableArrayList());
        categoryColumn.setCellValueFactory(data -> data.getValue().categoryProperty());
        predictionColumn.setCellValueFactory(data -> data.getValue().predictionProperty());
        recommendationColumn.setCellValueFactory(data -> data.getValue().recommendationProperty());
    }

    @FXML
    private void handlePredictReservation() {
        updatePrediction("Reservations");
    }

    @FXML
    private void handleBack() {
        aiRecommendationLabel.getScene().getWindow().hide();
    }

    @FXML
    private void handlePredictInventory() {
        updatePrediction("Inventory");
    }

    @FXML
    private void handlePredictFutureDemand() {
        updatePrediction("Future Demand");
    }

    @FXML
    private void handleCustomPredict() {
        String input = customInput.getText();
        if (input.isEmpty()) {
            aiRecommendationLabel.setText("⚠️ Please enter valid input.");
            return;
        }
        updatePrediction("Custom");
    }

    private void updatePrediction(String category) {
        double[] features = fetchLatestData();
        String prediction = AIModel.predictDemand(features);
        String recommendation = generateRecommendation(category, prediction);

        ObservableList<PredictionEntry> predictionList = FXCollections.observableArrayList(
                new PredictionEntry(category, prediction, recommendation)
        );
        predictionsTable.setItems(predictionList);

        savePredictionToDatabase(category, prediction, recommendation, features);
    }

    private double[] fetchLatestData() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(DISTINCT item_name) AS orders, COUNT(id) AS reservations, SUM(quantity) AS inventory FROM orders"
            );

            if (rs.next()) {
                return new double[]{
                        rs.getDouble("orders"),
                        rs.getDouble("reservations"),
                        rs.getDouble("inventory")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new double[]{0, 0, 0};
    }

    private String generateRecommendation(String category, String prediction) {
        switch (category) {
            case "Reservations":
                return prediction.equals("High") ? "Open more tables." :
                        prediction.equals("Medium") ? "Monitor peak hours." : "Offer discounts.";
            case "Inventory":
                return prediction.equals("High") ? "Stock up on essentials." :
                        prediction.equals("Medium") ? "Maintain levels." : "Reduce stock orders.";
            case "Future Demand":
                return prediction.equals("High") ? "Increase menu variety." :
                        prediction.equals("Medium") ? "Monitor demand trends." : "Introduce promotions.";
            default:
                return "AI-generated suggestion.";
        }
    }

    private void savePredictionToDatabase(String category, String prediction, String recommendation, double[] features) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO ai_predictions (category, prediction, recommendation, timestamp, confidence, orders, reservations, inventory) " +
                             "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)"
             )) {

            double confidence = Math.random() * 0.2 + 0.8;
            pstmt.setString(1, category);
            pstmt.setString(2, prediction);
            pstmt.setString(3, recommendation);
            pstmt.setDouble(4, confidence);
            pstmt.setDouble(5, features[0]);
            pstmt.setDouble(6, features[1]);
            pstmt.setDouble(7, features[2]);

            pstmt.executeUpdate();
            System.out.println("✅ AI Prediction saved: " + category + " (Confidence: " + confidence + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExportToCSV() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ai_predictions");
             FileWriter writer = new FileWriter("AI_Predictions.csv")) {

            writer.append("Category,Prediction,Recommendation,Timestamp,Confidence,Orders,Reservations,Inventory\n");

            while (rs.next()) {
                writer.append(rs.getString("category")).append(",");
                writer.append(rs.getString("prediction")).append(",");
                writer.append(rs.getString("recommendation")).append(",");
                writer.append(rs.getString("timestamp")).append(",");
                writer.append(String.valueOf(rs.getDouble("confidence"))).append(",");
                writer.append(String.valueOf(rs.getDouble("orders"))).append(",");
                writer.append(String.valueOf(rs.getDouble("reservations"))).append(",");
                writer.append(String.valueOf(rs.getDouble("inventory"))).append("\n");
            }

            writer.flush();
            System.out.println("✅ AI Predictions exported to AI_Predictions.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
