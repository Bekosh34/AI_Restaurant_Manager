package com.ai.restaurant.ai;

import com.ai.restaurant.model.TrainModel;
import com.ai.restaurant.managers.InventoryManager;
import com.ai.restaurant.managers.ReservationManager;
import com.ai.restaurant.managers.StaffManager;
import com.ai.restaurant.model.Inventory;
import com.ai.restaurant.model.Reservation;
import com.ai.restaurant.model.Staff;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.List;
import java.util.Map;

public class AIModel {
    private static Classifier model;
    private static Instances dataset;

    public static void trainModel() throws Exception {
        dataset = TrainModel.createEmptyDataset(); // Create dataset structure

        // ✅ Load training data from the database
        loadTrainingData();

        // ✅ Train the AI model
        model = new J48();
        model.buildClassifier(dataset);
        System.out.println("AI Model trained successfully with " + dataset.numInstances() + " instances.");
    }

    public static void trainReservationModel() throws Exception {
        dataset = TrainModel.createEmptyDataset();
        Map<String, Integer> reservationData = ReservationManager.getReservationCountsByDate();

        for (Map.Entry<String, Integer> entry : reservationData.entrySet()) {
            String date = entry.getKey();
            int reservationCount = entry.getValue();

            double[] features = {reservationCount}; // Feature: past reservation count
            Instance instance = new DenseInstance(2);
            instance.setValue(dataset.attribute(0), features[0]);
            instance.setValue(dataset.attribute(1), 1.0); // Class label

            dataset.add(instance);
        }

        model = new J48();
        model.buildClassifier(dataset);

        System.out.println("Reservation Prediction Model trained successfully!");
    }

    private static void loadTrainingData() {
        // ✅ Load Reservations
        List<Reservation> reservations = ReservationManager.getAllReservations();
        for (Reservation reservation : reservations) {
            double[] features = {reservation.getTableNumber(), 1.0};
            addInstance(features, 1.0); // Correct call
        }



    // ✅ Load Inventory
        List<Inventory> inventoryList = InventoryManager.getAllInventoryItems();
        for (Inventory item : inventoryList) {
            double[] features = {item.getQuantity(), 0.0};
            addInstance(features, 0.0);
        }

        // ✅ Load Staff
        List<Staff> staffList = StaffManager.getAllStaff();
        for (Staff staff : staffList) {
            double[] features = {staff.getSalary(), 1.0};
            addInstance(features, 1.0);
        }
    }

    private static void addInstance(double[] features, double classLabel) {
        Instance instance = new DenseInstance(features.length + 1); // +1 for class label
        for (int i = 0; i < features.length; i++) {
            instance.setValue(dataset.attribute(i), features[i]);
        }
        instance.setValue(dataset.attribute(features.length), classLabel);
        instance.setDataset(dataset);
        dataset.add(instance);
    }

    public static String predictReservation(double[] features) throws Exception {
        return predict(features, "High Demand", "Low Demand");
    }

    public static String predictInventory(double[] features) throws Exception {
        return predict(features, "Stock Low", "Stock Sufficient");
    }

    public static String predictMenuItem(double[] features) throws Exception {
        return predict(features, "Popular Item", "Less Popular Item");
    }

    public static String predictStaff(double[] features) throws Exception {
        return predict(features, "More Staff Needed", "Staffing Sufficient");
    }

    public static String predictFutureReservations(int expectedBookings) throws Exception {
        double[] features = {expectedBookings}; // Feature: projected booking count
        return predict(features, "High Demand", "Low Demand");
    }


    private static String predict(double[] features, String positiveOutcome, String negativeOutcome) throws Exception {
        if (model == null) {
            throw new IllegalStateException("Model not trained! Train the model before making predictions.");
        }

        Instance instance = new DenseInstance(features.length + 1);
        for (int i = 0; i < features.length; i++) {
            instance.setValue(dataset.attribute(i), features[i]);
        }
        instance.setDataset(dataset);

        double prediction = model.classifyInstance(instance);
        return prediction == 1.0 ? positiveOutcome : negativeOutcome;
    }
}
