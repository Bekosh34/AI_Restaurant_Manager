package com.ai.restaurant.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ReportsController {
    @FXML
    private TextArea reportArea;

    @FXML
    private void handleGenerateReport() {
        StringBuilder report = new StringBuilder();

        // Example AI-generated insights
        report.append("Reservation Insights: Predicted Peak Hour: 12 PM\n");
        report.append("Inventory Insights: Restock Tomatoes, Milk\n");
        report.append("Staff Insights: Add 2 Evening Shift Staff\n");

        reportArea.setText(report.toString());
    }
}
