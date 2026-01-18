package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation;

public record NumberValue(double v) implements Value {
    @Override public Object raw() { return v; }
}
