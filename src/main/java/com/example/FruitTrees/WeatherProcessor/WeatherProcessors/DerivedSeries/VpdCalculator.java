package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries;

import com.example.FruitTrees.Utilities.WeatherUtilities;
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
        // Always need dew point
        return List.of("dewpoint_2m");
    }

    @Override
    public List<String> optionalInputTypes() {
        // Prefer leaf temp if present, else fall back to air temp
        return List.of("leaf_temp_c", "temperature_2m");
    }

    @Override
    public double computeAt(double[] req, double[] opt) {
        double dewPtC = req[0];

        double tempC;
        if (opt.length > 0) {
            // If leaf temperature exists, it will be first
            tempC = opt[0];
        } else {
            // Safety fallback (shouldn't happen if temp is always provided)
            throw new IllegalStateException("No temperature available for VPD");
        }

        return WeatherUtilities.vaporPressureDeficit(tempC, dewPtC);
    }
}