package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component("InMemoryObservationCollector")
public class InMemoryObservationCollector implements ObservationCollector {
    private final List<Observation> observations = new ArrayList<>();

    @Override
    public void add(Observation observation) {
        observations.add(observation);
    }

    @Override
    public List<Observation> getAll() {
        return observations;
    }
}
