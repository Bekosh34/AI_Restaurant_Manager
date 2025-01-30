package com.ai.restaurant.gui;

public class PredictionEntry {
    private String category;
    private String prediction;
    private String recommendation;

    public PredictionEntry(String category, String prediction, String recommendation) {
        this.category = category;
        this.prediction = prediction;
        this.recommendation = recommendation;
    }

    public String getCategory() {
        return category;
    }

    public String getPrediction() {
        return prediction;
    }

    public String getRecommendation() {
        return recommendation;
    }
}
