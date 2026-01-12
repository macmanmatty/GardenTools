package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries;

import com.example.FruitTrees.Utilities.WeatherUtilities;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("CToF")
public class CToFCalculator implements DerivedSeriesCalculator {

    @Override
    public String outputType() {
        return "CtoF";
    }

    @Override
    public List<String> requiredInputTypes() {
        // Always need dew point
        return List.of("temperature_c");
    }

    @Override
    public List<String> optionalInputTypes() {
        // Prefer leaf temp if present, else fall back to air temp
        return List.of();
    }

    @Override
    public double computeAt(double[] req, double[] opt) {

        return WeatherUtilities.celsiusToFahrenheit(req[0]);
    }
}