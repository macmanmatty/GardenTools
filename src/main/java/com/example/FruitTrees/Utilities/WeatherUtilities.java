package com.example.FruitTrees.Utilities;

public class WeatherUtilities {

    /**
     * --- Unit Conversion Helpers ---
     */
    public static double fahrenheitToCelsius(double tempF) {
        return (tempF - 32.0) * 5.0 / 9.0;
    }

    public static double celsiusToFahrenheit(double tempC) {
        return tempC * 9.0 / 5.0 + 32.0;
    }


    /**
     * --- Relative Humidity from Temperature & Dew Point (Magnus Formula) ---
     *
     * @param tempC    Air temperature in °C
     * @param dewPtC   Dew point temperature in °C
     * @return Relative humidity as a percentage [0–100]
     *
     * Uses the Alduchov–Eskridge variant of the Magnus equation (common in meteorology).
     */
    public static double relativeHumidityFromTempAndDewpoint(double tempC, double dewPtC) {
        final double a = 17.625;
        final double b = 243.04; // °C

        double saturationVaporPressure = Math.exp((a * tempC) / (b + tempC));
        double actualVaporPressure     = Math.exp((a * dewPtC) / (b + dewPtC));

        double rhPercent = 100.0 * (actualVaporPressure / saturationVaporPressure);
        return Math.max(0.0, Math.min(100.0, rhPercent)); // Clamp to [0,100]
    }


    /**
     * --- Heat Index (NOAA Rothfusz Regression) ---
     *
     * @param tempF   Air temperature in °F
     * @param rh      Relative humidity in %
     * @return Heat index in °F
     *
     * Valid for T ≥ 80°F and RH ≥ 40%. Below those thresholds, returns T.
     * Includes NOAA's adjustment terms for low-RH/high-temp and high-RH/moderate-temp edges.
     */
    public static double heatIndexFahrenheit(double tempF, double rh) {
        if (tempF < 80.0 || rh < 40.0) {
            return tempF;
        }

        double T = tempF;
        double R = rh;

        double heatIndex = -42.379
                + 2.04901523 * T
                + 10.14333127 * R
                - 0.22475541 * T * R
                - 0.00683783 * T * T
                - 0.05481717 * R * R
                + 0.00122874 * T * T * R
                + 0.00085282 * T * R * R
                - 0.00000199 * T * T * R * R;

        // Adjustment for very low humidity and high temperature
        if (R < 13 && T >= 80 && T <= 112) {
            heatIndex -= ((13 - R) / 4.0) * Math.sqrt((17 - Math.abs(T - 95.0)) / 17.0);
        }
        // Adjustment for very high humidity and moderate temperature
        else if (R > 85 && T >= 80 && T <= 87) {
            heatIndex += ((R - 85) / 10.0) * ((87 - T) / 5.0);
        }

        return heatIndex;
    }


    /**
     * --- Wind Chill (NOAA Formula) ---
     *
     * @param tempF     Air temperature in °F
     * @param windMph   Wind speed in mph
     * @return Wind chill in °F
     *
     * Valid for T ≤ 50°F and wind speed ≥ 3 mph. Otherwise, returns T.
     */
    public static double windChillFahrenheit(double tempF, double windMph) {
        if (tempF > 50.0 || windMph < 3.0) {
            return tempF;
        }
        return 35.74
                + 0.6215 * tempF
                - 35.75 * Math.pow(windMph, 0.16)
                + 0.4275 * tempF * Math.pow(windMph, 0.16);
    }


    /**
     * --- Feels-like Temperature (Temp + Dew Point, no wind) ---
     *
     * @param tempF       Air temperature in °F
     * @param dewPointF   Dew point temperature in °F
     * @return Feels-like temperature in °F (uses heat index when hot/humid)
     */
    public static double feelsLikeTemperatureF(double tempF, double dewPointF) {
        double tempC  = fahrenheitToCelsius(tempF);
        double dewPtC = fahrenheitToCelsius(dewPointF);
        double rh     = relativeHumidityFromTempAndDewpoint(tempC, dewPtC);

        return heatIndexFahrenheit(tempF, rh);
    }


    /**
     * --- Feels-like Temperature (Temp + Dew Point + optional wind) ---
     *
     * @param tempF         Air temperature in °F
     * @param dewPointF     Dew point temperature in °F
     * @param windMphOrNull Wind speed in mph, or null if unavailable
     * @return Feels-like temperature in °F
     *
     * Uses wind chill for cold/windy conditions; heat index for hot/humid conditions; otherwise returns T.
     */
    public static double feelsLikeTemperatureF(double tempF, double dewPointF, Double windMphOrNull) {
        if (windMphOrNull == null) {
            return feelsLikeTemperatureF(tempF, dewPointF);
        }

        double windMph = windMphOrNull;

        if (tempF <= 50.0 && windMph >= 3.0) {
            return windChillFahrenheit(tempF, windMph);
        } else {
            return feelsLikeTemperatureF(tempF, dewPointF);
        }
    }


}
