package com.ai.restaurant.gui;

import com.ai.restaurant.utils.UserPreferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import com.ai.restaurant.model.Reservation;
import com.ai.restaurant.database.DatabaseManager;
import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML private StackPane rootPane;
    @FXML private ToggleButton darkModeToggle;
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> orderColumn;
    @FXML private TableColumn<Reservation, Integer> reservationColumn;
    @FXML private TableColumn<Reservation, Integer> inventoryColumn;
    @FXML private TableColumn<Reservation, String> predictionColumn;

    @FXML
    private void toggleDarkMode() {
        boolean isDark = darkModeToggle.isSelected();
        applyTheme(isDark);
        UserPreferences.saveDarkModePreference(isDark);
    }


    @FXML
    public void initialize() {
        applyTheme(UserPreferences.isDarkModeEnabled());
        darkModeToggle.setSelected(UserPreferences.isDarkModeEnabled());

        darkModeToggle.setOnAction(event -> {
            boolean isDark = darkModeToggle.isSelected();
            UserPreferences.saveDarkModePreference(isDark);
            applyTheme(isDark);
        });

        // Debugging: Ensure TableColumns are Linked
        if (orderColumn == null || reservationColumn == null || inventoryColumn == null || predictionColumn == null) {
            System.out.println("❌ ERROR: One or more TableColumns are null. Check FXML bindings!");
            return;
        }

        // Set up TableColumns
        orderColumn.setCellValueFactory(new PropertyValueFactory<>("orders"));
        reservationColumn.setCellValueFactory(new PropertyValueFactory<>("reservations"));
        inventoryColumn.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        predictionColumn.setCellValueFactory(new PropertyValueFactory<>("prediction"));

        loadReservations();
    }

    private void loadReservations() {
        List<Reservation> reservations = DatabaseManager.getReservations();
        reservationTable.setItems(FXCollections.observableArrayList(reservations));
    }

    private void applyTheme(boolean isDark) {
        if (rootPane == null || rootPane.getScene() == null) {
            return;
        }

        Scene scene = rootPane.getScene();
        scene.getStylesheets().clear();

        String themeFile = isDark ? "/dark-mode.css" : "/styles.css";
        scene.getStylesheets().add(getClass().getResource(themeFile).toExternalForm());

        // Apply theme to all UI elements
        rootPane.getStyleClass().removeAll("light-mode", "dark-mode");
        rootPane.getStyleClass().add(isDark ? "dark-mode" : "light-mode");

        darkModeToggle.setText(isDark ? "☀ Light Mode" : "🌙 Dark Mode");
    }


    @FXML
    private void handleReservationsButton() {
        openView("/ReservationView.fxml", "Manage Reservations", 800, 600);
    }

    private void openView(String fxmlPath, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInventoryButton() {
        openView("/InventoryView.fxml", "Manage Inventory");
    }

    @FXML
    private void handleStaffButton() {
        openView("/StaffView.fxml", "Manage Staff");
    }

    @FXML
    private void handleReportsButton() {
        openView("/ReportsView.fxml", "Reports & Analytics");
    }

    private void openView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
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