package com.ai.restaurant.ai;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AIModel {
    private static Classifier model;
    private static Instances dataset;
    private static final List<String> VALID_CLASSES = List.of("High", "Medium", "Low");

    public static void trainModel() {
        try {
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("orders"));
            attributes.add(new Attribute("reservations"));
            attributes.add(new Attribute("inventory"));

            ArrayList<String> classValues = new ArrayList<>(VALID_CLASSES);
            Attribute classAttribute = new Attribute("prediction", classValues);
            attributes.add(classAttribute);

            dataset = new Instances("TrainingData", attributes, 0);
            dataset.setClassIndex(dataset.numAttributes() - 1);

            System.out.println("📊 Training Dataset Attributes: " + dataset.numAttributes());

            loadTrainingData();

            if (dataset.numInstances() < 3) { // Ensure at least 3 instances to avoid training failures
                throw new Exception("Not enough training data! Ensure at least 3 records exist in ai_predictions.");
            }

            model = new RandomForest();
            model.buildClassifier(dataset);

            System.out.println("📂 Using Database Path: " + new File("src/main/resources/restaurant.db").getAbsolutePath());
            System.out.println("📂 Using Database Path: " + new File("src/main/resources/restaurant.db").getAbsolutePath());
            System.out.println("✅ AI Model trained successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to train AI Model.");
        }
    }

    private static void loadTrainingData() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT orders, reservations, inventory, prediction FROM ai_predictions");

            while (rs.next()) {
                double orders = rs.getDouble("orders");
                double reservations = rs.getDouble("reservations");
                double inventory = rs.getDouble("inventory");
                String fullPrediction = rs.getString("prediction");

                String cleanPrediction = extractLabel(fullPrediction);
                if (VALID_CLASSES.contains(cleanPrediction)) {
                    System.out.println("✅ Adding instance: Orders=" + orders + ", Reservations=" + reservations + ", Inventory=" + inventory + ", Prediction=" + cleanPrediction);
                    addTrainingInstance(new double[]{orders, reservations, inventory}, cleanPrediction);
                } else {
                    System.err.println("⚠️ Skipping invalid prediction: " + fullPrediction);
                }
            }

            if (dataset == null || dataset.numInstances() < 3) {
                System.err.println("⚠️ No valid training data found in the database.");
            } else {
                System.out.println("🔹 Training data loaded successfully. Instances: " + dataset.numInstances());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("⚠️ Failed to load training data.");
        }
    }

    private static String extractLabel(String fullPrediction) {
        for (String validClass : VALID_CLASSES) {
            if (fullPrediction.equalsIgnoreCase(validClass)) {
                return validClass;
            }
        }
        return "Unknown";
    }

    private static void addTrainingInstance(double[] features, String classValue) {
        if (dataset == null) {
            System.err.println("⚠️ Dataset is null, cannot add instance.");
            return;
        }

        DenseInstance instance = new DenseInstance(dataset.numAttributes());
        for (int i = 0; i < features.length; i++) {
            instance.setValue(dataset.attribute(i), features[i]);
        }
        instance.setValue(dataset.classAttribute(), classValue);
        instance.setDataset(dataset);
        dataset.add(instance);
    }

    public static boolean isModelTrained() {
        return model != null && dataset != null && dataset.numInstances() > 0;
    }

    public static String predictDemand(double[] features) {
        if (!isModelTrained()) {
            System.err.println("⚠️ Model is not trained, skipping prediction.");
            return "Model not trained";
        }

        try {
            System.out.println("🔍 Debug: Features received -> " + Arrays.toString(features));

            if (features.length != dataset.numAttributes() - 1) {
                System.err.println("⚠️ Feature size mismatch: Expected " + (dataset.numAttributes() - 1) + " but got " + features.length);
                return "Prediction Error";
            }

            DenseInstance instance = new DenseInstance(dataset.numAttributes());
            for (int i = 0; i < features.length; i++) {
                instance.setValue(dataset.attribute(i), features[i]);
            }
            instance.setDataset(dataset);

            double predictionIndex = model.classifyInstance(instance);
            return dataset.classAttribute().value((int) predictionIndex);

        } catch (Exception e) {
            e.printStackTrace();
            return "Prediction Error";
        }
    }
}
