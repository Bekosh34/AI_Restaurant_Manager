package com.ai.restaurant.gui;

import com.ai.restaurant.ai.AIModel;
import com.ai.restaurant.managers.ReservationManager;
import com.ai.restaurant.model.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ReservationController {

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    @FXML
    private TableColumn<Reservation, String> customerNameColumn;

    @FXML
    private TableColumn<Reservation, String> dateColumn;

    @FXML
    private TableColumn<Reservation, String> tableNumberColumn;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField tableNumberField;

    @FXML
    private Label feedbackLabel;

    private ObservableList<Reservation> reservationList;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));

        loadReservations();
    }

    private void loadReservations() {
        reservationList = FXCollections.observableArrayList(ReservationManager.getAllReservations());
        reservationTable.setItems(reservationList);
    }

    @FXML
    private void handleAddReservation() {
        String customerName = customerNameField.getText();
        String date = dateField.getText();

        if (customerName.isEmpty() || date.isEmpty() || tableNumberField.getText().isEmpty()) {
            feedbackLabel.setText("Error: All fields must be filled!");
            return;
        }

        try {
            int tableNumber = Integer.parseInt(tableNumberField.getText()); // Convert String to int
            Reservation reservation = new Reservation(0, customerName, date, tableNumber);
            if (ReservationManager.addReservation(reservation)) {
                feedbackLabel.setText("Reservation added successfully!");
                loadReservations();
            } else {
                feedbackLabel.setText("Error: Unable to add reservation.");
            }
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Error: Table number must be a valid number.");
        }
    }

    @FXML
    private Label aiRecommendationLabel;

    @FXML
    private void generateAISuggestions() {
        try {
            // Example features: table number and some fixed value (can customize)
            double[] features = {Double.parseDouble(tableNumberField.getText()), 1.0};
            String prediction = AIModel.predictReservation(features);
            aiRecommendationLabel.setText("AI Suggestion: " + prediction);
        } catch (Exception e) {
            aiRecommendationLabel.setText("Error generating suggestions.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDeleteReservation() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            feedbackLabel.setText("Error: No reservation selected.");
            return;
        }

        try {
            int reservationId = selectedReservation.getId(); // Ensure this is an int
            if (ReservationManager.deleteReservation(reservationId)) {
                feedbackLabel.setText("Reservation deleted successfully!");
                loadReservations();
            } else {
                feedbackLabel.setText("Error: Unable to delete reservation.");
            }
        } catch (Exception e) {
            feedbackLabel.setText("Error: Invalid reservation data.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        reservationTable.getScene().getWindow().hide();
    }
}

