package com.ai.restaurant.gui;

import com.ai.restaurant.model.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StaffController {

    @FXML
    private TextField staffNameField;

    @FXML
    private TextField roleField;

    @FXML
    private TableView<Staff> staffTable;

    @FXML
    private TableColumn<Staff, Integer> idColumn;

    @FXML
    private TableColumn<Staff, String> staffNameColumn;

    @FXML
    private TableColumn<Staff, String> roleColumn;

    private ObservableList<Staff> staffList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        staffNameColumn.setCellValueFactory(new PropertyValueFactory<>("staffName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        staffTable.setItems(staffList);
    }

    @FXML
    private void handleAddStaff() {
        String staffName = staffNameField.getText();
        String role = roleField.getText();

        if (staffName.isEmpty() || role.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        Staff staff = new Staff(staffList.size() + 1, staffName, role);
        staffList.add(staff);

        staffNameField.clear();
        roleField.clear();
    }

    @FXML
    private void handleDeleteStaff() {
        Staff selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            staffList.remove(selected);
        } else {
            showAlert("Error", "No staff selected!");
        }
    }

    @FXML
    private void handleBack() {
        staffTable.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
