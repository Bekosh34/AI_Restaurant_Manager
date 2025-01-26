package com.ai.restaurant.ai;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class AIModel {
    private static Classifier reservationModel;
    private static Classifier inventoryModel;
    private static Classifier staffModel;

    static {
        try {
            // Load pre-trained models (generated using Weka or similar tools)
            reservationModel = (Classifier) weka.core.SerializationHelper.read("reservation.model");
            inventoryModel = (Classifier) weka.core.SerializationHelper.read("inventory.model");
            staffModel = (Classifier) weka.core.SerializationHelper.read("staff.model");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String predictReservation(double[] features) {
        return predict(reservationModel, features);
    }

    public static String predictInventory(double[] features) {
        return predict(inventoryModel, features);
    }

    public static String predictStaff(double[] features) {
        return predict(staffModel, features);
    }

    private static String predict(Classifier model, double[] features) {
        try {
            DataSource source = new DataSource("data.arff");
            Instances data = source.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);

            Instance instance = data.get(0); // Create a new instance
            for (int i = 0; i < features.length; i++) {
                instance.setValue(i, features[i]);
            }

            double prediction = model.classifyInstance(instance);
            return "Prediction: " + prediction;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to make prediction.";
        }
    }
}
