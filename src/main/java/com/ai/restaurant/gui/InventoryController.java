package com.ai.restaurant.gui;

import com.ai.restaurant.ai.AIModel;
import com.ai.restaurant.managers.InventoryManager;
import com.ai.restaurant.model.Inventory;
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

    private ObservableList<Inventory> inventoryList;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadInventoryData();

        // Debugging
        if (feedbackLabel == null) {
            System.out.println("FeedbackLabel is null!");
        } else {
            System.out.println("FeedbackLabel is initialized.");
        }
    }

    private void loadInventoryData() {
        inventoryList = FXCollections.observableArrayList(InventoryManager.getAllInventoryItems());
        inventoryTable.setItems(inventoryList);
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
        } else {
            feedbackLabel.setText("Error: Unable to delete item.");
        }
    }

    @FXML
    private Label aiRecommendationLabel;

    @FXML
    private void generateAIInventorySuggestions() {
        try {
            // Example features: quantity
            double[] features = {Double.parseDouble(quantityField.getText()), 1.0};
            String prediction = AIModel.predictInventory(features);
            aiRecommendationLabel.setText("AI Suggestion: " + prediction);
        } catch (Exception e) {
            aiRecommendationLabel.setText("Error generating inventory suggestions.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBack() {
        inventoryTable.getScene().getWindow().hide();
    }
}
