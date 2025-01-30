package com.ai.restaurant.gui;

import com.ai.restaurant.ai.AIModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;

public class ReportsController {

    @FXML
    private Label aiRecommendationLabel;
    @FXML
    private TableView<PredictionEntry> predictionsTable;
    @FXML
    private TableColumn<PredictionEntry, String> categoryColumn;
    @FXML
    private TableColumn<PredictionEntry, String> predictionColumn;
    @FXML
    private TableColumn<PredictionEntry, String> recommendationColumn;
    @FXML
    private TextField customInput;

    @FXML
    private void handlePredictReservation() {
        predictAndUpdate("Reservations", AIModel::predictReservation);
    }

    @FXML
    private void handlePredictInventory() {
        predictAndUpdate("Inventory", AIModel::predictInventory);
    }

    @FXML
    private void handlePredictFutureDemand() {
        predictAndUpdate("Future Demand", AIModel::predictFutureDemand);
    }

    private void predictAndUpdate(String category, PredictionFunction predictionFunction) {
        try {
            double[] features = {1.0}; // Example feature
            String prediction = predictionFunction.predict(features);
            updateTable(category, prediction, getRecommendation(prediction));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTable(String category, String prediction, String recommendation) {
        predictionsTable.getItems().add(new PredictionEntry(category, prediction, recommendation));
    }

    private String getRecommendation(String prediction) {
        switch (prediction) {
            case "High Demand": return "Increase staff & stock.";
            case "Low Demand": return "Normal operations.";
            case "Stock Low": return "Restock inventory immediately.";
            case "Stock Sufficient": return "Inventory levels are fine.";
            case "More Staff Needed": return "Hire temporary workers.";
            case "Staffing Sufficient": return "Staffing levels are good.";
            default: return "No specific recommendation.";
        }
    }

    @FunctionalInterface
    interface PredictionFunction {
        String predict(double[] features) throws Exception;
    }
}
