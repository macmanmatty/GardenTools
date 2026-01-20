package com.example.FruitTrees.Metrics.Observation;

import java.util.List;

public interface ObservationCollector {
    void add(Observation observation);
    List<Observation> getAll();
}
