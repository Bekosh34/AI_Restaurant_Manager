package com.ai.restaurant.gui;

import com.ai.restaurant.ai.AIModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StaffController {
    @FXML
    private Label aiStaffPredictionLabel;

    public void initialize() {
        updateAIStaffPredictions();
    }

    private void updateAIStaffPredictions() {
        double[] features = {10.0, 5.0}; // Example features (current workload, shift time)
        String prediction = AIModel.predictStaff(features);
        aiStaffPredictionLabel.setText(prediction);
    }
}
