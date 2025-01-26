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

        try {
            // Train the AI model during startup
            System.out.println("Training AI model...");
            AIModel.trainModel();
            System.out.println("AI Model trained successfully!");

            // Load the main view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading the main view: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error initializing the AI model: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
