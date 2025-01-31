package com.ai.restaurant.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import com.ai.restaurant.model.Reservation;
import com.ai.restaurant.managers.ReservationManager;

public class ReservationController {
    @FXML private TextField customerNameField;
    @FXML private TextField dateField;
    @FXML private TextField tableNumberField;
    @FXML private TableView<Reservation> reservationTable;
    @FXML private Label feedbackLabel;

    @FXML
    private void handleAddReservation() {
        String customerName = customerNameField.getText();
        String date = dateField.getText();
        String tableNumber = tableNumberField.getText();
        if (customerName.isEmpty() || date.isEmpty() || tableNumber.isEmpty()) {
            feedbackLabel.setText("⚠️ Please fill all fields!");
            return;
        }
        ReservationManager.addReservation(new Reservation(0, customerName, date, Integer.parseInt(tableNumber)));
        refreshReservationTable();
    }

    @FXML
    private void handleDeleteReservation() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            ReservationManager.deleteReservation(selectedReservation.getId());
            refreshReservationTable();
        }
    }

    @FXML
    private void handleBack() {
        feedbackLabel.getScene().getWindow().hide();
    }

    private void refreshReservationTable() {
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList(ReservationManager.getAllReservations());
        reservationTable.setItems(reservationList);
    }
}