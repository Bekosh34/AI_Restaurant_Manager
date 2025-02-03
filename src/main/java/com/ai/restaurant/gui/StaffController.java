package com.ai.restaurant.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import com.ai.restaurant.model.Staff;
import com.ai.restaurant.managers.StaffManager;

public class StaffController {
    @FXML private TextField nameField;
    @FXML private TextField roleField;
    @FXML private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, Integer> idColumn;
    @FXML private TableColumn<Staff, String> nameColumn;
    @FXML private TableColumn<Staff, String> roleColumn;
    @FXML private Label feedbackLabel;

    @FXML
    private void handleAddStaff() {
        String name = nameField.getText();
        String role = roleField.getText();
        if (name.isEmpty() || role.isEmpty()) {
            feedbackLabel.setText("⚠️ Please fill in all fields!");
            return;
        }
        Staff newStaff = new Staff(0, name, role);
        StaffManager.addStaff(newStaff);
        refreshStaffTable();
    }

    @FXML
    private void handleDeleteStaff() {
        Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {
            StaffManager.deleteStaff(selectedStaff.getId());
            refreshStaffTable();
        }
    }

    @FXML
    private void handleUpdateRole() {
        Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {
            selectedStaff.setRole(roleField.getText());
            StaffManager.updateStaff(selectedStaff);
            refreshStaffTable();
        }
    }

    @FXML
    private void handleBack() {
        feedbackLabel.getScene().getWindow().hide();
    }

    private void refreshStaffTable() {
        ObservableList<Staff> staffList = FXCollections.observableArrayList(StaffManager.getAllStaff());
        staffTable.setItems(staffList);
    }
}