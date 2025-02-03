package com.ai.restaurant.gui;

import com.ai.restaurant.database.DatabaseManager;
import com.ai.restaurant.model.Reservation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.util.List;

public class ReservationController {

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, String> customerColumn;
    @FXML private TableColumn<Reservation, String> dateColumn;
    @FXML private TableColumn<Reservation, Integer> tableNumberColumn;
    @FXML private TextField customerField;
    @FXML private TextField dateField;
    @FXML private TextField tableNumberField;
    @FXML private Label feedbackLabel;

    @FXML
    public void initialize() {
        // Debugging: Ensure TableColumns are Linked
        if (idColumn == null || customerColumn == null || dateColumn == null || tableNumberColumn == null) {
            System.out.println("❌ ERROR: One or more TableColumns are null. Check FXML bindings!");
            return;
        }

        // Set up TableColumns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));

        loadReservations();
    }

    private void loadReservations() {
        List<Reservation> reservations = DatabaseManager.getReservations();
        System.out.println("✅ Reservations Loaded: " + reservations);
        reservationTable.setItems(FXCollections.observableArrayList(reservations));
    }

    @FXML
    private void handleAddReservation() {
        String customerName = customerField.getText();
        String date = dateField.getText();
        String tableNumberText = tableNumberField.getText();

        if (customerName.isEmpty() || date.isEmpty() || tableNumberText.isEmpty()) {
            feedbackLabel.setText("❌ All fields are required!");
            return;
        }

        try {
            int tableNumber = Integer.parseInt(tableNumberText);
            DatabaseManager.addReservation(customerName, date, tableNumber);
            feedbackLabel.setText("✅ Reservation added successfully!");
            loadReservations();
            clearFields();
        } catch (NumberFormatException e) {
            feedbackLabel.setText("❌ Table Number must be a number!");
        }
    }

    @FXML
    private void handleDeleteReservation() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            DatabaseManager.deleteReservation(selectedReservation.getId());
            feedbackLabel.setText("✅ Reservation deleted!");
            loadReservations();
        } else {
            feedbackLabel.setText("❌ No reservation selected!");
        }
    }

    private void clearFields() {
        customerField.clear();
        dateField.clear();
        tableNumberField.clear();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
