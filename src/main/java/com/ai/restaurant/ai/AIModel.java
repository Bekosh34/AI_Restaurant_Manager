package com.ai.restaurant.ai;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AIModel {
    private static Classifier model;
    private static Instances dataset;
    private static final List<String> VALID_CLASSES = List.of("High", "Medium", "Low"); // Ensure only valid classes

    public static void trainModel() {
        try {
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("confidence")); // Use confidence as a feature

            // Define allowed class values
            ArrayList<String> classValues = new ArrayList<>(VALID_CLASSES);
            Attribute classAttribute = new Attribute("prediction", classValues);
            attributes.add(classAttribute);

            dataset = new Instances("TrainingData", attributes, 0);
            dataset.setClassIndex(dataset.numAttributes() - 1);

            // Load training data from database
            loadTrainingData();

            // Ensure enough data exists before training
            if (dataset.numInstances() == 0) {
                throw new Exception("Not enough training data!");
            }

            model = new RandomForest();
            model.buildClassifier(dataset);

            System.out.println("✅ AI Model trained successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to train AI Model.");
        }
    }

    private static void loadTrainingData() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(
                    "SELECT id, category, prediction, recommendation FROM ai_predictions"
            );


            while (rs.next()) {
                double confidence = rs.getDouble("confidence");
                String fullPrediction = rs.getString("prediction");

                // Extract only "High", "Medium", or "Low"
                String prediction = extractLabel(fullPrediction);

                if (VALID_CLASSES.contains(prediction)) {
                    addTrainingInstance(new double[]{confidence}, prediction);
                } else {
                    System.err.println("⚠️ Skipping invalid prediction: " + fullPrediction);
                }
            }

            System.out.println("🔹 Training data loaded successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("⚠️ Failed to load training data.");
        }
    }

    /**
     * Extracts only "High", "Medium", or "Low" from a full prediction string.
     */
    private static String extractLabel(String fullPrediction) {
        for (String validClass : VALID_CLASSES) {
            if (fullPrediction.contains(validClass)) {
                return validClass;
            }
        }
        return "Unknown"; // Default case for invalid values
    }


    private static void addTrainingInstance(double[] features, String classValue) {
        DenseInstance instance = new DenseInstance(features.length + 1);
        for (int i = 0; i < features.length; i++) {
            instance.setValue(dataset.attribute(i), features[i]);
        }
        instance.setValue(dataset.classAttribute(), classValue);
        instance.setDataset(dataset);
        dataset.add(instance);
    }

    public static String predictDemand(double[] features) {
        try {
            if (model == null || dataset == null) {
                return "Model not trained";
            }

            DenseInstance instance = new DenseInstance(features.length + 1);
            for (int i = 0; i < features.length; i++) {
                instance.setValue(dataset.attribute(i), features[i]);
            }
            instance.setDataset(dataset);

            double prediction = model.classifyInstance(instance);
            return dataset.classAttribute().value((int) prediction);

        } catch (Exception e) {
            e.printStackTrace();
            return "Prediction Error";
        }
    }
}
