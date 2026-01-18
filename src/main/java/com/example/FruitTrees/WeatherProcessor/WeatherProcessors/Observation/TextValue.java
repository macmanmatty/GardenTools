package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation;

public record TextValue(String v) implements Value {
    @Override public Object raw() { return v; }
}
