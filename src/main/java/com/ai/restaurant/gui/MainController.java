package com.ai.restaurant.gui;

import com.ai.restaurant.utils.UserPreferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML private StackPane rootPane;

    @FXML
    private void handleReservationsButton() {
        openView("/fxml/ReservationView.fxml", "Manage Reservations");
    }

    @FXML
    private void handleInventoryButton() {
        openView("/fxml/InventoryView.fxml", "Manage Inventory");
    }

    @FXML
    private void handleStaffButton() {
        openView("/fxml/StaffView.fxml", "Manage Staff");
    }

    @FXML
    private void handleReportsButton() {
        openView("/fxml/ReportsView.fxml", "Reports & Analytics");
    }



    private void openView(String fxmlPath, String title) {
        try {
            URL resourceUrl = getClass().getResource(fxmlPath);
            if (resourceUrl == null) {
                System.err.println("❌ ERROR: FXML file not found: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
