package com.ai.restaurant.ai;

import com.ai.restaurant.model.TrainModel;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class AIModel {
    private static Classifier model;
    private static Instances dataset;

    public static void trainModel() throws Exception {
        dataset = TrainModel.createEmptyDataset();
        model = new J48();
        model.buildClassifier(dataset);
        System.out.println("✅ AI Model trained successfully!");
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

    private static String predict(double[] features, String positiveOutcome, String negativeOutcome) throws Exception {
        if (model == null) {
            throw new IllegalStateException("Model not trained!");
        }

        Instance instance = new DenseInstance(features.length + 1);
        for (int i = 0; i < features.length; i++) {
            instance.setValue(TrainModel.createEmptyDataset().attribute(i), features[i]);
        }
        instance.setDataset(TrainModel.createEmptyDataset());

        double prediction = model.classifyInstance(instance);
        return prediction == 1.0 ? positiveOutcome : negativeOutcome;
    }
}
