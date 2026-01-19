package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries;

import com.example.FruitTrees.Utilities.WeatherUtilities;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeelsLikeFahrenheitCalculatorTest {
    @Test
    void declaresMetadataCorrectly() {
        DerivedSeriesCalculator calc = new FeelsLikeFahrenheitCalculator();

        assertEquals("feels_like_f", calc.outputType());
        assertEquals(List.of("temperature_f", "dewpoint_f"), calc.requiredInputTypes());
        assertEquals(List.of("wind_mph"), calc.optionalInputTypes());
    }
    @Test
    void usesWindWhenPresent() {
        DerivedSeriesCalculator calc = new FeelsLikeFahrenheitCalculator();
        double tempF = 35.0;
        double dewF  = 20.0;
        double wind  = 15.0;

        double expected = WeatherUtilities.feelsLikeTemperatureF(tempF, dewF, wind);

        double actual = calc.computeAt(new double[]{tempF, dewF}, new double[]{wind});

        assertEquals(expected, actual, 1e-9);
    }

@Test
    void fallsBackWhenWindMissing() {
        DerivedSeriesCalculator calc = new FeelsLikeFahrenheitCalculator();

        double tempF = 92.0;
        double dewF  = 75.0;

        double expected = WeatherUtilities.feelsLikeTemperatureF(tempF, dewF, null);

        double actual = calc.computeAt(new double[]{tempF, dewF}, new double[0]);

        assertEquals(expected, actual, 1e-9);
    }
}
