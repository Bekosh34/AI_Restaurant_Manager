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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadStaffData();
    }

    private void loadStaffData() {
        staffList = FXCollections.observableArrayList(StaffManager.getAllStaff());
        staffTable.setItems(staffList);
    }

    @FXML
    private Label aiRecommendationLabel;

    @FXML
    private void generateAIStaffSuggestions() {
        try {
            // Example features: salary (you can use other features like role or hours)
            double[] features = {Double.parseDouble(roleField.getText()), 1.0};
            String prediction = AIModel.predictStaff(features);
            aiRecommendationLabel.setText("AI Suggestion: " + prediction);
        } catch (Exception e) {
            aiRecommendationLabel.setText("Error generating staff suggestions.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddStaff() {
        String name = nameField.getText();
        String role = roleField.getText();

        if (name.isEmpty() || role.isEmpty()) {
            feedbackLabel.setText("Error: All fields must be filled!");
            return;
        }

        Staff staff = new Staff(0, name, role, 0.0); // Replace 0.0 with an actual salary if available
        ; // ID is 0 as it’s auto-generated
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
    private void handleBack() {
        staffTable.getScene().getWindow().hide();
    }
}