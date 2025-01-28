package com.ai.restaurant.gui;

import com.ai.restaurant.ai.AIModel;
import com.ai.restaurant.model.AIReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ReportsController {

    @FXML
    private Label aiRecommendationLabel;

    @FXML
    private TableView<AIReport> aiReportTable;

    @FXML
    private TableColumn<AIReport, String> colCategory;

    @FXML
    private TableColumn<AIReport, String> colPrediction;

    @FXML
    private TableColumn<AIReport, String> colRecommendation;

    @FXML
    private TextField inputField;

    private ObservableList<AIReport> reportData;

    @FXML
    public void initialize() {
        reportData = FXCollections.observableArrayList();
        aiReportTable.setItems(reportData);

        colCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        colPrediction.setCellValueFactory(cellData -> cellData.getValue().predictionProperty());
        colRecommendation.setCellValueFactory(cellData -> cellData.getValue().recommendationProperty());
    }

    @FXML
    private void handlePredictReservations() {
        double[] features = {1.0}; // Example input features
        try {
            String prediction = AIModel.predictReservation(features);
            aiRecommendationLabel.setText("Reservation Prediction: " + prediction);
            addReport("Reservations", prediction, generateRecommendation(prediction));
        } catch (Exception e) {
            aiRecommendationLabel.setText("Error predicting reservations.");
        }
    }

    @FXML
    private void handlePredictInventory() {
        double[] features = {1.0}; // Example input features
        try {
            String prediction = AIModel.predictInventory(features);
            aiRecommendationLabel.setText("Inventory Prediction: " + prediction);
            addReport("Inventory", prediction, generateRecommendation(prediction));
        } catch (Exception e) {
            aiRecommendationLabel.setText("Error predicting inventory.");
        }
    }

    @FXML
    private void handlePredictStaff() {
        double[] features = {1.0}; // Example input features
        try {
            String prediction = AIModel.predictStaff(features);
            aiRecommendationLabel.setText("Staff Prediction: " + prediction);
            addReport("Staff", prediction, generateRecommendation(prediction));
        } catch (Exception e) {
            aiRecommendationLabel.setText("Error predicting staff.");
        }
    }

    @FXML
    private void handlePredictFutureDemand() {
        try {
            String prediction = AIModel.predictFutureReservations(10); // Example: Predict for 10 bookings
            aiRecommendationLabel.setText("Future Demand Prediction: " + prediction);
        } catch (Exception e) {
            e.printStackTrace();
            aiRecommendationLabel.setText("Error predicting future demand.");
        }
    }


    @FXML
    private void handleCustomPrediction() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) {
            aiRecommendationLabel.setText("Please enter a valid query.");
            return;
        }

        try {
            double[] features = {Math.random() * 10}; // Example conversion logic
            String prediction = AIModel.predictMenuItem(features);
            aiRecommendationLabel.setText("Custom Prediction: " + prediction);
            addReport("Custom Query: " + input, prediction, generateRecommendation(prediction));
        } catch (Exception e) {
            aiRecommendationLabel.setText("Error processing custom input.");
        }
    }


    private void addReport(String category, String prediction, String recommendation) {
        reportData.add(new AIReport(category, prediction, recommendation));
    }

    private String generateRecommendation(String prediction) {
        if (prediction.equals("High Demand")) return "Increase staff & stock.";
        if (prediction.equals("Stock Low")) return "Restock inventory immediately.";
        if (prediction.equals("More Staff Needed")) return "Hire temporary workers.";
        return "No immediate action required.";
    }

    @FXML
    private void handleBack() {
        aiReportTable.getScene().getWindow().hide();
    }
}
