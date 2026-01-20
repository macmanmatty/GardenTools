package com.example.FruitTrees.Metrics;

public enum Period {
    DAILY("Daily"),
    MONTHLY("Monthly"),
    YEARLY("Yearly");

    private final String label;

    Period(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
