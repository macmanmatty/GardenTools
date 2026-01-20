package com.example.FruitTrees.Metrics.Observation;

public record NumberValue(double v) implements Value {
    @Override public Object raw() { return v; }
}
