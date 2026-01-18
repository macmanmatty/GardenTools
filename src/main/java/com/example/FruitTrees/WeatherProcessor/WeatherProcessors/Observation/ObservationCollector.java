package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation;

import java.util.List;

public interface ObservationCollector {
    void add(Observation observation);
    List<Observation> getAll();
}
