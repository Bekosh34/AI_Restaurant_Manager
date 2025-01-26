package com.ai.restaurant.gui;

import com.ai.restaurant.managers.InventoryManager;
import com.ai.restaurant.model.Inventory;
import com.ai.restaurant.ai.AIModel; // Use AIModel from the ai package
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryController {

    @FXML
    private TableView<Inventory> inventoryTable;

    @FXML
    private TableColumn<Inventory, Integer> idColumn;

    @FXML
    private TableColumn<Inventory, String> itemNameColumn;

    @FXML
    private TableColumn<Inventory, Integer> quantityColumn;

    @FXML
    private TextField itemNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Label aiInventoryLabel; // For AI suggestions

    private ObservableList<Inventory> inventoryList;

    @FXML
    public void initialize() {
        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Load inventory data
        loadInventoryData();

        // Update AI suggestions
        updateAIInventorySuggestions();
    }

    private void loadInventoryData() {
        inventoryList = FXCollections.observableArrayList(InventoryManager.getAllInventoryItems());
        inventoryTable.setItems(inventoryList);
    }

    @FXML
    private void handleGenerateInventoryInsights() {
        try {
            double[] currentFeatures = {5.0, 10.0}; // Example features: stock levels and reorder history
            String prediction = AIModel.predictInventory(currentFeatures);
            aiInventoryLabel.setText("AI Suggestion: " + prediction);
        } catch (Exception e) {
            aiInventoryLabel.setText("AI Error: Unable to generate suggestions.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAddItem() {
        String itemName = itemNameField.getText();
        String quantityText = quantityField.getText();

        if (itemName.isEmpty() || quantityText.isEmpty()) {
            feedbackLabel.setText("Error: All fields must be filled!");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            Inventory inventory = new Inventory(0, itemName, quantity); // ID is 0 as it’s auto-generated
            if (InventoryManager.addInventoryItem(inventory)) {
                feedbackLabel.setText("Item added successfully!");
                loadInventoryData();
                updateAIInventorySuggestions();
            } else {
                feedbackLabel.setText("Error: Unable to add item.");
            }
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Error: Quantity must be a number.");
        }
    }

    @FXML
    private void handleUpdateQuantity() {
        Inventory selectedInventory = inventoryTable.getSelectionModel().getSelectedItem();
        String quantityText = quantityField.getText();

        if (selectedInventory == null) {
            feedbackLabel.setText("Error: No item selected.");
            return;
        }

        if (quantityText.isEmpty()) {
            feedbackLabel.setText("Error: Quantity field must be filled.");
            return;
        }

        try {
            int newQuantity = Integer.parseInt(quantityText);
            if (InventoryManager.updateInventoryQuantity(selectedInventory.getId(), newQuantity)) {
                feedbackLabel.setText("Quantity updated successfully!");
                loadInventoryData();
                updateAIInventorySuggestions();
            } else {
                feedbackLabel.setText("Error: Unable to update quantity.");
            }
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Error: Quantity must be a number.");
        }
    }

    @FXML
    private void handleDeleteItem() {
        Inventory selectedInventory = inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedInventory == null) {
            feedbackLabel.setText("Error: No item selected.");
            return;
        }

        if (InventoryManager.deleteInventoryItem(selectedInventory.getId())) {
            feedbackLabel.setText("Item deleted successfully!");
            loadInventoryData();
            updateAIInventorySuggestions();
        } else {
            feedbackLabel.setText("Error: Unable to delete item.");
        }
    }

    @FXML
    private void handleGenerateInventoryInsights() {
        try {
            // Example features: [current stock level, reorder threshold]
            double[] currentFeatures = {averageStockLevel(), reorderThreshold()};
            String prediction = AIModel.predictInventory(currentFeatures);
            aiInventoryLabel.setText("AI Suggestion: " + prediction);
        } catch (Exception e) {
            aiInventoryLabel.setText("AI Error: Unable to generate suggestions.");
            e.printStackTrace();
        }
    }

    private double averageStockLevel() {
        return inventoryList.stream().mapToInt(Inventory::getQuantity).average().orElse(0.0);
    }

    private double reorderThreshold() {
        return 10.0; // Example: Define threshold dynamically based on usage
    }

    @FXML
    private void handleBack() {
        inventoryTable.getScene().getWindow().hide(); // Close the current window
    }
}
