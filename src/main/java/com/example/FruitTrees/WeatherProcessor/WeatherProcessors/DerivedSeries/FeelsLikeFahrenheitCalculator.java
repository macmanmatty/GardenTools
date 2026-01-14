package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries;

import com.example.FruitTrees.Utilities.WeatherUtilities;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("feels_like_f")
public class FeelsLikeFahrenheitCalculator implements DerivedSeriesCalculator {

    @Override
    public String outputType() {
        return "feels_like_2m";
    }

    @Override
    public List<String> requiredInputTypes() {
        return List.of("temperature_2m", "dewpoint_2m");
    }

    @Override
    public List<String> optionalInputTypes() {
        return List.of("wind_mph");
    }

    @Override
    public double computeAt(double[] req, double[] opt) {
        double tempF = req[0];
        double dewF  = req[1];

        Double wind = (opt.length >= 1) ? opt[0] : null;
        return WeatherUtilities.feelsLikeTemperatureF(tempF, dewF, wind);
    }
}
