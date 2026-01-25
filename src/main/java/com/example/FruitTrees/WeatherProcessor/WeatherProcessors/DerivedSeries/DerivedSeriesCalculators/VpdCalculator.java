package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries.DerivedSeriesCalculators;

import com.example.FruitTrees.Metrics.Unit;
import com.example.FruitTrees.Utilities.WeatherUtilities;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries.DerivedSeriesCalculator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("vpd_kpa")
public class VpdCalculator implements DerivedSeriesCalculator {

    @Override
    public String outputType() {
        return "vpd_kpa";
    }

    @Override
    public List<String> requiredInputTypes() {
        return List.of("temperature_2m", "dewpoint_2m");
    }

    @Override
    public List<Unit> preferredUnits() {
        return List.of();
    }

    @Override
    public List<String> optionalInputTypes() {
        return List.of("leaf_temp");
    }

    @Override
    public double computeAt(double[] req, double[] opt) {
        double airTempC = req[0];
        double dewPtC   = req[1];

        double tempC = (opt.length > 0) ? opt[0] : airTempC;

        return WeatherUtilities.vaporPressureDeficit(tempC, dewPtC);
    }
}