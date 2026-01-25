package com.example.FruitTrees.Metrics;

import com.example.FruitTrees.Utilities.WeatherUtilities;

/**
 * Central physical canonicalUnit conversion utility.
 * Uses strongly-typed Unit enums internally (no magic strings).
 */
public final class Units {

    public static double convert(double value, Unit from, Unit to) {
        if (from == to) return value;
        if (from == Unit.UNKNOWN || to == Unit.UNKNOWN) return value; // or throw, your call

        if (from.dimension() != to.dimension()) {
            throw new IllegalArgumentException("Incompatible conversion: " + from + " -> " + to);
        }

        return switch (from.dimension()) {
            case TEMPERATURE -> convertTemperature(value, from, to);
            case WIND_SPEED -> convertWindSpeed(value, from, to);
            case PRESSURE -> convertPressure(value, from, to);
            case LENGTH -> convertDepth(value, from, to);
            default-> value; // identity families
        };
    }

    // ---------------- Temperature ----------------

    private static double convertTemperature(double v, Unit from, Unit to) {
        if (from == Unit.DEG_C && to == Unit.DEG_F) return WeatherUtilities.celsiusToFahrenheit(v);
        if (from == Unit.DEG_F && to == Unit.DEG_C) return WeatherUtilities.fahrenheitToCelsius(v);

        if (from == Unit.DEG_C && to == Unit.DEG_K) return WeatherUtilities.celsiusToKelvin(v);
        if (from == Unit.DEG_K && to == Unit.DEG_C) return WeatherUtilities.kelvinToCelsius(v);

        if (from == Unit.DEG_F && to == Unit.DEG_K) return WeatherUtilities.fahrenheitToKelvin(v);
        if (from == Unit.DEG_K && to == Unit.DEG_F) return WeatherUtilities.kelvinToFahrenheit(v);

        throw new IllegalArgumentException("Temperature canonicalUnit not supported: " + from + " -> " + to);
    }

    // ---------------- Wind Speed ----------------

    private static double convertWindSpeed(double v, Unit from, Unit to) {
        if (from == Unit.M_PER_S && to == Unit.MPH)  return WeatherUtilities.metersPerSecondToMilesPerHour(v);
        if (from == Unit.MPH && to == Unit.M_PER_S)  return WeatherUtilities.milesPerHourToMetersPerSecond(v);

        if (from == Unit.M_PER_S && to == Unit.KM_PER_H) return WeatherUtilities.metersPerSecondToKilometersPerHour(v);
        if (from == Unit.KM_PER_H && to == Unit.M_PER_S) return WeatherUtilities.kilometersPerHourToMetersPerSecond(v);

        if (from == Unit.MPH && to == Unit.KM_PER_H) return WeatherUtilities.milesPerHourToKilometersPerHour(v);
        if (from == Unit.KM_PER_H && to == Unit.MPH) return WeatherUtilities.kilometersPerHourToMilesPerHour(v);

        throw new IllegalArgumentException("Wind speed canonicalUnit not supported: " + from + " -> " + to);
    }

    // ---------------- Pressure ----------------

    private static double convertPressure(double v, Unit from, Unit to) {
        if (from == Unit.PA  && to == Unit.HPA) return WeatherUtilities.pascalsToHectoPascals(v);
        if (from == Unit.HPA && to == Unit.PA)  return WeatherUtilities.hectoPascalsToPascals(v);

        if (from == Unit.PA  && to == Unit.KPA) return WeatherUtilities.pascalsToKiloPascals(v);
        if (from == Unit.KPA && to == Unit.PA)  return WeatherUtilities.kiloPascalsToPascals(v);

        if (from == Unit.HPA && to == Unit.KPA) return WeatherUtilities.hectoPascalsToKiloPascals(v);
        if (from == Unit.KPA && to == Unit.HPA) return WeatherUtilities.kiloPascalsToHectoPascals(v);

        if (from == Unit.PA   && to == Unit.INHG) return WeatherUtilities.pascalsToInchesOfMercury(v);
        if (from == Unit.INHG && to == Unit.PA)   return WeatherUtilities.inchesOfMercuryToPascals(v);

        if (from == Unit.HPA  && to == Unit.INHG) return WeatherUtilities.hectoPascalsToInchesOfMercury(v);
        if (from == Unit.INHG && to == Unit.HPA)  return WeatherUtilities.inchesOfMercuryToHectoPascals(v);

        if (from == Unit.PA  && to == Unit.PSI) return WeatherUtilities.pascalsToPsi(v);
        if (from == Unit.PSI && to == Unit.PA)  return WeatherUtilities.psiToPascals(v);

        if (from == Unit.HPA && to == Unit.PSI) return WeatherUtilities.hectoPascalsToPsi(v);
        if (from == Unit.PSI && to == Unit.HPA) return WeatherUtilities.psiToHectoPascals(v);

        throw new IllegalArgumentException("Pressure canonicalUnit not supported: " + from + " -> " + to);
    }

    // ---------------- Depth ----------------

    private static double convertDepth(double v, Unit from, Unit to) {
        if (from == Unit.MM && to == Unit.IN) return v / 25.4;
        if (from == Unit.IN && to == Unit.MM) return v * 25.4;

        throw new IllegalArgumentException("Depth canonicalUnit not supported: " + from + " -> " + to);
    }

    private Units() {}
}
