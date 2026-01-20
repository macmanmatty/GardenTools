package com.example.FruitTrees.Metrics.Observation;

public record TextValue(String v) implements Value {
    @Override public Object raw() { return v; }
}
