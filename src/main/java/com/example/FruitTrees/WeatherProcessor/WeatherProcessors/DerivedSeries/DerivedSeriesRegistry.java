package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class DerivedSeriesRegistry {

    private final Map<String, DerivedSeriesCalculator> calculators;

    @Autowired
    public DerivedSeriesRegistry(Map<String, DerivedSeriesCalculator> calculators) {
        this.calculators = calculators;
    }

    /**
     * Returns the derived series calculator for the given canonical key,
     * or null if the key is not a derived series.
     */
    public DerivedSeriesCalculator getCalculatorOrNull(String key) {
        return calculators.get(key);
    }

    public Collection<DerivedSeriesCalculator> getAll() {
        return calculators.values();
    }
}
