package com.ai.restaurant.gui;

import com.ai.restaurant.managers.ReservationManager;
import com.ai.restaurant.model.Reservation;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ReservationController {

    @FXML
    private TextField customerNameField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField tableNumberField;

    @FXML
    private void handleAddReservation() {
        String customerName = customerNameField.getText();
        String date = dateField.getText();
        int tableNumber = Integer.parseInt(tableNumberField.getText());

        Reservation newReservation = new Reservation(0, customerName, date, tableNumber);
        if (ReservationManager.addReservation(newReservation)) {
            System.out.println("✅ Reservation added successfully!");
        } else {
            System.out.println("❌ Failed to add reservation.");
        }
    }

    @FXML
    private void handleDeleteReservation() {
        int id = Integer.parseInt(tableNumberField.getText()); // Assuming input field is used for ID
        if (ReservationManager.deleteReservation(id)) {
            System.out.println("✅ Reservation deleted successfully!");
        } else {
            System.out.println("❌ Failed to delete reservation.");
        }
    }
}
