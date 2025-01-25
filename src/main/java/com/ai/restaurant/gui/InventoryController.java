package com.ai.restaurant.gui;

import com.ai.restaurant.model.InventoryItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryController {

    @FXML
    private TextField itemNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<InventoryItem> inventoryTable;

    @FXML
    private TableColumn<InventoryItem, Integer> idColumn;

    @FXML
    private TableColumn<InventoryItem, String> itemNameColumn;

    @FXML
    private TableColumn<InventoryItem, Integer> quantityColumn;

    private ObservableList<InventoryItem> inventoryList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Link data to the table
        inventoryTable.setItems(inventoryList);
    }

    @FXML
    private void handleAddItem() {
        String itemName = itemNameField.getText();
        String quantityText = quantityField.getText();

        if (itemName.isEmpty() || quantityText.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            InventoryItem item = new InventoryItem(inventoryList.size() + 1, itemName, quantity);
            inventoryList.add(item);

            // Clear input fields
            itemNameField.clear();
            quantityField.clear();
        } catch (NumberFormatException e) {
            showAlert("Error", "Quantity must be a valid number!");
        }
    }

    @FXML
    private void handleDeleteItem() {
        InventoryItem selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            inventoryList.remove(selected);
        } else {
            showAlert("Error", "No item selected!");
        }
    }

    @FXML
    private void handleBack() {
        inventoryTable.getScene().getWindow().hide(); // Close current window
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
