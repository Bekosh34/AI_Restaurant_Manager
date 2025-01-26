package com.ai.restaurant.model;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;

public class TrainModel {

    private static Instances dataset;

    public static Instances createEmptyDataset() {
        if (dataset == null) {
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("feature1")); // Example feature 1
            attributes.add(new Attribute("feature2")); // Example feature 2
            attributes.add(new Attribute("class")); // Class attribute (target)

            dataset = new Instances("TrainingData", attributes, 0);
            dataset.setClassIndex(attributes.size() - 1);
        }
        return dataset;
    }

    public static void addInstance(Instances dataset, double[] features, double classValue) {
        DenseInstance instance = new DenseInstance(features.length + 1);
        for (int i = 0; i < features.length; i++) {
            instance.setValue(dataset.attribute(i), features[i]);
        }
        instance.setValue(dataset.attribute(features.length), classValue);
        instance.setDataset(dataset);
        dataset.add(instance);
    }
}
