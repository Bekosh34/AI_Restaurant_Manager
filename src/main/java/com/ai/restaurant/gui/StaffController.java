package com.ai.restaurant.gui;

import com.ai.restaurant.ai.AIModel;
import com.ai.restaurant.managers.StaffManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class StaffController {

    @FXML
    private TextField salaryField;
    @FXML
    private Label predictionLabel;

    @FXML
    private void handlePredictStaff() {
        try {
            double salary = Double.parseDouble(salaryField.getText());
            double[] features = {salary}; // Staff salary as feature

            String prediction = AIModel.predictStaff(features);
            predictionLabel.setText("Staff Prediction: " + prediction);

        } catch (Exception e) {
            e.printStackTrace();
            predictionLabel.setText("Error predicting staff needs.");
        }
    }
}
