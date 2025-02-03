package com.ai.restaurant.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import com.ai.restaurant.model.Staff;
import com.ai.restaurant.database.DatabaseManager;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class StaffController {
    @FXML private TextField nameField;
    @FXML private TextField roleField;
    @FXML private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, Integer> idColumn;
    @FXML private TableColumn<Staff, String> nameColumn;
    @FXML private TableColumn<Staff, String> roleColumn;
    @FXML private Label feedbackLabel;

    @FXML
    public void initialize() {
        if (idColumn == null || nameColumn == null || roleColumn == null) {
            System.out.println("❌ ERROR: One or more TableColumns are null. Check FXML bindings!");
            return;
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        refreshStaffTable();
    }

    @FXML
    private void handleAddStaff() {
        String name = nameField.getText();
        String role = roleField.getText();

        if (name.isEmpty() || role.isEmpty()) {
            feedbackLabel.setText("⚠️ Please fill in all fields!");
            return;
        }

        // Create a new Staff object and pass it to the DatabaseManager
        Staff newStaff = new Staff(0, name, role);
        DatabaseManager.addStaff(newStaff);

        feedbackLabel.setText("✅ Staff added successfully!");
        refreshStaffTable();
    }


    @FXML
    private void handleDeleteStaff() {
        Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {
            DatabaseManager.deleteStaff(selectedStaff.getId());
            feedbackLabel.setText("✅ Staff member deleted!");
            refreshStaffTable();
        } else {
            feedbackLabel.setText("❌ No staff member selected!");
        }
    }

    @FXML
    private void handleUpdateRole() {
        Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {
            String newRole = roleField.getText();
            if (newRole.isEmpty()) {
                feedbackLabel.setText("⚠️ Role field cannot be empty!");
                return;
            }
            selectedStaff.setRole(newRole);
            DatabaseManager.updateStaff(selectedStaff);
            feedbackLabel.setText("✅ Role updated successfully!");
            refreshStaffTable();
        } else {
            feedbackLabel.setText("❌ No staff member selected!");
        }
    }

    private void refreshStaffTable() {
        List<Staff> staffList = DatabaseManager.getAllStaff();
        staffTable.setItems(FXCollections.observableArrayList(staffList));
    }

    private void clearFields() {
        nameField.clear();
        roleField.clear();
    }

    @FXML
    private void handleBack() {
        staffTable.getScene().getWindow().hide();
    }
}
