package com.example.FruitTrees.Metrics.Observation;

import java.time.LocalDateTime;

public final class Values {
    private Values() {}

    public static Value number(double v) { return new NumberValue(v); }
    public static Value time(LocalDateTime v) { return new TimeValue(v); }
    public static Value text(String v) { return new TextValue(v); }
}

