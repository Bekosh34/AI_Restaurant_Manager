package com.ai.restaurant.model;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TrainModel {
    public static void main(String[] args) throws Exception {
        // Load the dataset (ARFF file stored in resources/datasets)
        DataSource source = new DataSource("src/main/resources/datasets/reservation_data.arff");
        Instances dataset = source.getDataSet();

        // Set the class index (the last attribute in the ARFF file)
        dataset.setClassIndex(dataset.numAttributes() - 1);

        // Train a decision tree model (J48 in Weka)
        J48 tree = new J48();
        tree.buildClassifier(dataset);

        // Save the trained model to a file
        weka.core.SerializationHelper.write("src/main/resources/models/reservation_model.model", tree);
        System.out.println("Model trained and saved successfully!");
    }
}
