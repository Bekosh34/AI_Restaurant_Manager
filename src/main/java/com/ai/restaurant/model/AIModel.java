package com.ai.restaurant.model;

import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class AIModel {
    public static String predict(double[] features) throws Exception {
        // Load the trained model
        Classifier model = (Classifier) weka.core.SerializationHelper.read("src/main/resources/models/reservation_model.model");

        // Define the dataset structure (you must define the attributes as in the ARFF)
        Instances dataset = TrainModel.createEmptyDataset();
        dataset.setClassIndex(dataset.numAttributes() - 1);

        // Create an instance with the given features
        Instance instance = new DenseInstance(features.length + 1);
        instance.setDataset(dataset);
        for (int i = 0; i < features.length; i++) {
            instance.setValue(dataset.attribute(i), features[i]);
        }

        // Predict the class
        double prediction = model.classifyInstance(instance);
        return dataset.classAttribute().value((int) prediction);
    }
}
