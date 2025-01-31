package com.ai.restaurant.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PredictionEntry {
    private final StringProperty category;
    private final StringProperty prediction;
    private final StringProperty recommendation;

    public PredictionEntry(String category, String prediction, String recommendation) {
        this.category = new SimpleStringProperty(category);
        this.prediction = new SimpleStringProperty(prediction);
        this.recommendation = new SimpleStringProperty(recommendation);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty predictionProperty() {
        return prediction;
    }

    public StringProperty recommendationProperty() {
        return recommendation;
    }
}
