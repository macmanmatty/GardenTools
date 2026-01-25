package com.example.FruitTrees.File;

import com.example.FruitTrees.Utilities.WeatherUtilities;

public final class Units {


    private static double convertTemp(String from, String to, double v) {
        if (from.equalsIgnoreCase("degC") && to.equalsIgnoreCase("degF"))
            return WeatherUtilities.celsiusToFahrenheit(v);

        if (from.equalsIgnoreCase("degF") && to.equalsIgnoreCase("degC"))
            return WeatherUtilities.fahrenheitToCelsius(v);

        throw new IllegalArgumentException("Temp canonicalUnit not supported: " + from + " -> " + to);
    }

    private static double convertWind(String from, String to, double v) {
        if (from.equalsIgnoreCase("m/s") && to.equalsIgnoreCase("mph"))
            return WeatherUtilities.metersPerSecondToMilesPerHour(v);

        if (from.equalsIgnoreCase("mph") && to.equalsIgnoreCase("m/s"))
            return WeatherUtilities.milesPerHourToMetersPerSecond(v);

        if (from.equalsIgnoreCase("m/s") && to.equalsIgnoreCase("km/h"))
            return WeatherUtilities.metersPerSecondToKilometersPerHour(v);

        if (from.equalsIgnoreCase("km/h") && to.equalsIgnoreCase("m/s"))
            return WeatherUtilities.kilometersPerHourToMetersPerSecond(v);

        if (from.equalsIgnoreCase("mph") && to.equalsIgnoreCase("km/h"))
            return WeatherUtilities.milesPerHourToKilometersPerHour(v);

        if (from.equalsIgnoreCase("km/h") && to.equalsIgnoreCase("mph"))
            return WeatherUtilities.kilometersPerHourToMilesPerHour(v);

        throw new IllegalArgumentException("Wind canonicalUnit not supported: " + from + " -> " + to);
    }

    private static double convertPressure(String from, String to, double v) {
        if (from.equalsIgnoreCase("Pa") && to.equalsIgnoreCase("hPa"))
            return WeatherUtilities.pascalsToHectoPascals(v);

        if (from.equalsIgnoreCase("hPa") && to.equalsIgnoreCase("Pa"))
            return WeatherUtilities.hectoPascalsToPascals(v);

        if (from.equalsIgnoreCase("Pa") && to.equalsIgnoreCase("kPa"))
            return WeatherUtilities.pascalsToKiloPascals(v);

        if (from.equalsIgnoreCase("kPa") && to.equalsIgnoreCase("Pa"))
            return WeatherUtilities.kiloPascalsToPascals(v);

        if (from.equalsIgnoreCase("hPa") && to.equalsIgnoreCase("kPa"))
            return WeatherUtilities.hectoPascalsToKiloPascals(v);

        if (from.equalsIgnoreCase("kPa") && to.equalsIgnoreCase("hPa"))
            return WeatherUtilities.kiloPascalsToHectoPascals(v);

        if (from.equalsIgnoreCase("Pa") && to.equalsIgnoreCase("inHg"))
            return WeatherUtilities.pascalsToInchesOfMercury(v);

        if (from.equalsIgnoreCase("inHg") && to.equalsIgnoreCase("Pa"))
            return WeatherUtilities.inchesOfMercuryToPascals(v);

        if (from.equalsIgnoreCase("hPa") && to.equalsIgnoreCase("inHg"))
            return WeatherUtilities.hectoPascalsToInchesOfMercury(v);

        if (from.equalsIgnoreCase("inHg") && to.equalsIgnoreCase("hPa"))
            return WeatherUtilities.inchesOfMercuryToHectoPascals(v);

        if (from.equalsIgnoreCase("Pa") && to.equalsIgnoreCase("psi"))
            return WeatherUtilities.pascalsToPsi(v);

        if (from.equalsIgnoreCase("psi") && to.equalsIgnoreCase("Pa"))
            return WeatherUtilities.psiToPascals(v);

        if (from.equalsIgnoreCase("hPa") && to.equalsIgnoreCase("psi"))
            return WeatherUtilities.hectoPascalsToPsi(v);

        if (from.equalsIgnoreCase("psi") && to.equalsIgnoreCase("hPa"))
            return WeatherUtilities.psiToHectoPascals(v);

        throw new IllegalArgumentException("Pressure canonicalUnit not supported: " + from + " -> " + to);
    }

    private static double convertDepth(String from, String to, double v) {
        if (from.equalsIgnoreCase("mm") && to.equalsIgnoreCase("in"))
            return v / 25.4;

        if (from.equalsIgnoreCase("in") && to.equalsIgnoreCase("mm"))
            return v * 25.4;

        throw new IllegalArgumentException("Depth canonicalUnit not supported: " + from + " -> " + to);
    }

    private Units() {}
}
