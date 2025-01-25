package com.ai.restaurant.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    @FXML
    private void handleReservationButton() {
        loadFXML("/fxml/ReservationView.fxml", "Manage Reservations");
    }

    @FXML
    private void handleInventoryButton() {
        loadFXML("/fxml/InventoryView.fxml", "Manage Inventory");
    }

    @FXML
    private void handleStaffButton() {
        loadFXML("/fxml/StaffView.fxml", "Manage Staff");
    }

    @FXML
    private void handleReportsButton() {
        loadFXML("/fxml/ReportsView.fxml", "Generate Reports");
    }

    private void loadFXML(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
