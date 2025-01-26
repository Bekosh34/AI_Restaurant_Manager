package com.ai.restaurant.gui;

import com.ai.restaurant.ai.AIModel;
import com.ai.restaurant.managers.StaffManager;
import com.ai.restaurant.model.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StaffController {

    @FXML
    private TableView<Staff> staffTable;

    @FXML
    private TableColumn<Staff, Integer> idColumn;

    @FXML
    private TableColumn<Staff, String> nameColumn;

    @FXML
    private TableColumn<Staff, String> roleColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField roleField;

    @FXML
    private Label feedbackLabel;

    private ObservableList<Staff> staffList;

    @FXML
    public void initialize() {
        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Load staff data
        loadStaffData();
    }

    private void loadStaffData() {
        staffList = FXCollections.observableArrayList(StaffManager.getAllStaff());
        staffTable.setItems(staffList);
    }

    @FXML
    private void handleAddStaff() {
        String name = nameField.getText();
        String role = roleField.getText();

        if (name.isEmpty() || role.isEmpty()) {
            feedbackLabel.setText("Error: All fields must be filled!");
            return;
        }

        Staff staff = new Staff(0, name, role); // ID is 0 as it’s auto-generated
        if (StaffManager.addStaff(staff)) {
            feedbackLabel.setText("Staff member added successfully!");
            loadStaffData();
        } else {
            feedbackLabel.setText("Error: Unable to add staff member.");
        }
    }

    @FXML
    private void handleUpdateRole() {
        Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        String newRole = roleField.getText();

        if (selectedStaff == null) {
            feedbackLabel.setText("Error: No staff member selected.");
            return;
        }

        if (newRole.isEmpty()) {
            feedbackLabel.setText("Error: Role field must be filled.");
            return;
        }

        if (StaffManager.updateStaffRole(selectedStaff.getId(), newRole)) {
            feedbackLabel.setText("Role updated successfully!");
            loadStaffData();
        } else {
            feedbackLabel.setText("Error: Unable to update role.");
        }
    }

    @FXML
    private void handleDeleteStaff() {
        Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();

        if (selectedStaff == null) {
            feedbackLabel.setText("Error: No staff member selected.");
            return;
        }

        if (StaffManager.deleteStaff(selectedStaff.getId())) {
            feedbackLabel.setText("Staff member deleted successfully!");
            loadStaffData();
        } else {
            feedbackLabel.setText("Error: Unable to delete staff member.");
        }
    }

    @FXML
    private Label aiStaffLabel; // Add this in your FXML for AI suggestions

    @FXML
    private void handleOptimizeStaffSchedule() {
        try {
            // Example features: current shift hour and demand level
            double[] currentFeatures = {3.0, 2.0}; // Example: shift hour and current demand
            String prediction = AIModel.predictReservation(currentFeatures); // Reuse AIModel for prediction
            aiStaffLabel.setText("AI Suggestion: " + prediction);
        } catch (Exception e) {
            aiStaffLabel.setText("AI Error: Unable to generate suggestions.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOptimizeStaffSchedule() {
        try {
            double[] currentFeatures = {3.0, 2.0}; // Example: shift hour and demand level
            String prediction = AIModel.predictStaff(currentFeatures);
            feedbackLabel.setText("AI Suggestion: " + prediction);
        } catch (Exception e) {
            feedbackLabel.setText("AI Error: Unable to generate suggestions.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBack() {
        staffTable.getScene().getWindow().hide(); // Close the current window
    }
}
