package com.ai.restaurant.gui;

import com.ai.restaurant.ai.AIModel;
import com.ai.restaurant.database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryController {
    @FXML
    private TextField itemNameField;
    @FXML
    private TextField stockField;
    @FXML
    private Label aiPredictionLabel;

    @FXML
    private void handleAddItem() {
        String itemName = itemNameField.getText();
        int stock = Integer.parseInt(stockField.getText());

        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO inventory (item_name, stock) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, itemName);
            statement.setInt(2, stock);
            statement.executeUpdate();

            updateAIPredictions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAIPredictions() {
        double[] features = {30, 50}; // Example features (current stock, avg usage)
        String prediction = AIModel.predictInventory(features);
        aiPredictionLabel.setText(prediction);
    }
}
