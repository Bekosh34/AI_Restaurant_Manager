package com.ai.restaurant.gui;

import com.ai.restaurant.database.DatabaseManager;
import com.ai.restaurant.model.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationController {

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField tableNumberField;

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    @FXML
    private TableColumn<Reservation, String> customerNameColumn;

    @FXML
    private TableColumn<Reservation, String> dateColumn;

    @FXML
    private TableColumn<Reservation, Integer> tableNumberColumn;

    private ObservableList<Reservation> reservationList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));

        // Load data from the database
        loadReservations();
        reservationTable.setItems(reservationList);
    }

    private void loadReservations() {
        reservationList.clear();
        String query = "SELECT * FROM reservations";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reservationList.add(new Reservation(
                        rs.getInt("id"),
                        rs.getString("customer_name"),gi
                        rs.getString("date"),
                        rs.getInt("table_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAddReservation() {
        String customerName = customerNameField.getText();
        String date = dateField.getText();
        String tableNumberText = tableNumberField.getText();

        if (customerName.isEmpty() || date.isEmpty() || tableNumberText.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        try {
            int tableNumber = Integer.parseInt(tableNumberText);

            String query = "INSERT INTO reservations (customer_name, date, table_number) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseManager.connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, customerName);
                stmt.setString(2, date);
                stmt.setInt(3, tableNumber);
                stmt.executeUpdate();
            }

            // Refresh table
            loadReservations();
            customerNameField.clear();
            dateField.clear();
            tableNumberField.clear();
        } catch (NumberFormatException e) {
            showAlert("Error", "Table Number must be a valid number!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "No reservation selected!");
            return;
        }

        String query = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();

            // Refresh the table
            loadReservations();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete reservation!");
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Parent mainView = loader.load();

            // Get current stage and set the new scene
            Stage stage = (Stage) reservationTable.getScene().getWindow();
            stage.setScene(new Scene(mainView));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
