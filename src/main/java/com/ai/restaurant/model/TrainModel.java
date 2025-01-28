package com.ai.restaurant.model;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

public class TrainModel {

    public static Instances createEmptyDataset() {
        Attribute attr1 = new Attribute("Feature1");
        Attribute attr2 = new Attribute("Feature2");
        FastVector classValues = new FastVector(2);
        classValues.addElement("Positive");
        classValues.addElement("Negative");
        Attribute classAttr = new Attribute("Class", classValues);

        FastVector attributes = new FastVector(3);
        attributes.addElement(attr1);
        attributes.addElement(attr2);
        attributes.addElement(classAttr);

        Instances dataset = new Instances("Dataset", attributes, 0);
        dataset.setClassIndex(dataset.numAttributes() - 1);

        return dataset;
    }

    public static void addInstance(Instances dataset, double[] features, double classValue) {
        DenseInstance instance = new DenseInstance(features.length + 1);
        for (int i = 0; i < features.length; i++) {
            instance.setValue(dataset.attribute(i), features[i]);
        }
        instance.setValue(dataset.classAttribute(), classValue);
        dataset.add(instance);
    }
}
