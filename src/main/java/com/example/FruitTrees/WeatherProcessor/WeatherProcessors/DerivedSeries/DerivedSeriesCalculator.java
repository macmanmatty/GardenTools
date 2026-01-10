package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries;

import java.util.List;

public interface DerivedSeriesCalculator {
    String outputType();
    List<String> requiredInputTypes();
    default List<String> optionalInputTypes() { return List.of(); }
    double computeAt(double[] requiredInputs, double[] optionalInputs);
}
