package com.example.FruitTrees.Metrics;

import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestUnitNormalizerTest {

    private static final double EPS = 1e-6;

    @Test
    void normalize_temperatureThresholds_fromF_toCanonicalC() {
        // Arrange: user wants to think in Fahrenheit
        WeatherRequest req = new WeatherRequest();
        req.setTemperatureUnit("degF");
        req.setWindSpeedUnit("mph");
        req.setPressureUnit("inHg");
        req.setPrecipitationUnit("in");

        HourlyWeatherProcessRequest cfg = new HourlyWeatherProcessRequest();
        cfg.setHourlyDataType("temperature_2m"); // internal type or something DataUtilities maps to it
        cfg.setLowerBound(32.0);                 // 32°F
        cfg.setUpperBound(50.0);                 // 50°F
        cfg.setThreshold(40.0);                  // 40°F
    req.getHourlyWeatherProcessRequests().add(cfg);
        // Act
        RequestUnitNormalizer.normalizeHourlyConfigsToCanonical(req);

        // Assert: canonical for temperature_2m is degC per MetricRegistry
        assertEquals(0.0,  cfg.getLowerBound(), EPS);      // 32F -> 0C
        assertEquals(10.0, cfg.getUpperBound(), EPS);      // 50F -> 10C
        assertEquals(4.444444, cfg.getThreshold(), 1e-4);  // 40F -> 4.444C
    }

    @Test
    void normalize_windThresholds_fromMph_toCanonicalMps() {
        WeatherRequest req = new WeatherRequest();
        req.setTemperatureUnit("degF"); // irrelevant
        req.setWindSpeedUnit("mph");    // this matters
        req.setPressureUnit("hPa");
        req.setPrecipitationUnit("mm");

        HourlyWeatherProcessRequest cfg = new HourlyWeatherProcessRequest();
        cfg.setHourlyDataType("wind_speed_10m"); // canonical m/s
        cfg.setThreshold(10.0);                  // 10 mph
        req.getHourlyWeatherProcessRequests().add(cfg);

        RequestUnitNormalizer.normalizeHourlyConfigsToCanonical(req);

        // 10 mph = 4.4704 m/s
        assertEquals(4.4704, cfg.getThreshold(), 1e-4);
    }

    @Test
    void normalize_precipDepth_fromIn_toCanonicalMm() {
        WeatherRequest req = new WeatherRequest();
        req.setTemperatureUnit("degC");
        req.setWindSpeedUnit("m/s");
        req.setPressureUnit("hPa");
        req.setPrecipitationUnit("in"); // user thinks in inches

        HourlyWeatherProcessRequest cfg = new HourlyWeatherProcessRequest();
        cfg.setHourlyDataType("rain");   // canonical mm
        cfg.setThreshold(1.0);           // 1 inch
        req.getHourlyWeatherProcessRequests().add(cfg);

        RequestUnitNormalizer.normalizeHourlyConfigsToCanonical(req);

        assertEquals(25.4, cfg.getThreshold(), EPS);
    }

    @Test
    void normalize_missingUnitPreference_throws() {
        WeatherRequest req = new WeatherRequest();
        // Intentionally do not set windSpeedUnit (or set it to nonsense)
        req.setWindSpeedUnit("bananas");

        HourlyWeatherProcessRequest cfg = new HourlyWeatherProcessRequest();
        cfg.setHourlyDataType("wind_speed_10m");
        cfg.setThreshold(10.0);
        req.getHourlyWeatherProcessRequests().add(cfg);


        assertThrows(IllegalArgumentException.class,
                () -> RequestUnitNormalizer.normalizeHourlyConfigsToCanonical(req));
    }

}
