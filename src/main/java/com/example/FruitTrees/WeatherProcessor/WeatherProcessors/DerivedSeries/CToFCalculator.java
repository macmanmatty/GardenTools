package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries;

import com.example.FruitTrees.Utilities.WeatherUtilities;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("CtoF")
public class CToFCalculator implements DerivedSeriesCalculator {

    @Override
    public String outputType() {
        return "vpd_kpa";
    }

    @Override
    public List<String> requiredInputTypes() {
        // Always need dew point
        return List.of("temperature_f");
    }

    @Override
    public List<String> optionalInputTypes() {
        // Prefer leaf temp if present, else fall back to air temp
        return List.of();
    }

    @Override
    public double computeAt(double[] req, double[] opt) {

        double tempF;
        if (opt.length > 0) {
            // If leaf temperature exists, it will be first
            tempF = opt[0];
        } else {
            // Safety fallback (shouldn't happen if temp is always provided)
            throw new IllegalStateException("No temperature available");
        }

        return WeatherUtilities.fahrenheitToCelsius(tempF);
    }
}