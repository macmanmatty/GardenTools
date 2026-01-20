package com.example.FruitTrees.Metrics;

public enum Stat {

    // No aggregation, raw value
    BASE("Value"),

    // Measures of center
    MEAN("Mean"),
    MEDIAN("Median"),
    MODE("Mode"),

    // Extremes
    MIN("Min"),
    MAX("Max"),

    // Spread
    STD_DEV("Std Dev"),
    VARIANCE("Variance"),
    IQR("IQR"),

    // Percentiles
    P05("5th Percentile"),
    P10("10th Percentile"),
    P25("25th Percentile"),
    P50("50th Percentile"),   // same as median
    P75("75th Percentile"),
    P90("90th Percentile"),
    P95("95th Percentile"),
    P99("99th Percentile");

    private final String label;

    Stat(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public boolean isPercentile() {
        return name().startsWith("P");
    }
}
