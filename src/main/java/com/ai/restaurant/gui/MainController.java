package com.ai.restaurant.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void handleReservationsButton() {
        openView("/fxml/ReservationView.fxml");
    }

    @FXML
    private void handleInventoryButton() {
        openView("/fxml/InventoryView.fxml");
    }

    @FXML
    private void handleStaffButton() {
        openView("/fxml/StaffView.fxml");
    }

    @FXML
    private void handleReportsButton() {
        openView("/fxml/ReportsView.fxml");
    }

    private void openView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
