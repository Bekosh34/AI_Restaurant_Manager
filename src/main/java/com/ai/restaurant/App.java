package com.ai.restaurant;

import com.ai.restaurant.ai.AIModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AI Restaurant Manager");

        // Initialize and train the AI model
        try {
            AIModel.trainModel();
            System.out.println("AI Model trained successfully!");
        } catch (Exception e) {
            System.out.println("Error initializing the AI model: " + e.getMessage());
            e.printStackTrace();
        }

        // Load MainView
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Error loading the main view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
