package com.ai.restaurant.managers;

import com.ai.restaurant.gui.PredictionEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReportsManager {
    public static ObservableList<PredictionEntry> getPredictions(String category) {
        ObservableList<PredictionEntry> predictions = FXCollections.observableArrayList();
        predictions.add(new PredictionEntry(category, "High", "Increase stock"));
        predictions.add(new PredictionEntry(category, "Medium", "Maintain stock"));
        predictions.add(new PredictionEntry(category, "Low", "Reduce stock"));
        return predictions;
    }
}