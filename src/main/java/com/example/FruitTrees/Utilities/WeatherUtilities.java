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
     * --- Temperature Conversion Helpers (Absolute) ---
     *
     * 0 °C = 273.15 K
     */
    public static double celsiusToKelvin(double tempC) {
        return tempC + 273.15;
    }

    public static double kelvinToCelsius(double tempK) {
        return tempK - 273.15;
    }

    public static double fahrenheitToKelvin(double tempF) {
        return celsiusToKelvin(fahrenheitToCelsius(tempF));
    }

    public static double kelvinToFahrenheit(double tempK) {
        return celsiusToFahrenheit(kelvinToCelsius(tempK));
    }


    public static double mphToMetersPerSecond(double mph) {
        return mph * 0.44704;
    }

    public static double metersPerSecondToMph(double ms) {
        return ms / 0.44704;
    }
    /**
     * --- Pressure Conversion Helpers ---
     *
     * Base relationships:
     * 1 atmosphere = 101325 Pa
     * 1 bar        = 100000 Pa
     * 1 hPa        = 100 Pa   (same as millibar, mb)
     * 1 kPa        = 1000 Pa
     * 1 inHg       = 3386.389 Pa (standard gravity, 0°C)
     * 1 psi        = 6894.757 Pa
     */

    // Pascals ↔ hectopascals (millibars)
    public static double pascalsToHectoPascals(double pa) {
        return pa / 100.0;
    }

    public static double hectoPascalsToPascals(double hPa) {
        return hPa * 100.0;
    }

    // Pascals ↔ kilopascals
    public static double pascalsToKiloPascals(double pa) {
        return pa / 1000.0;
    }

    public static double kiloPascalsToPascals(double kPa) {
        return kPa * 1000.0;
    }

    // hPa ↔ kPa (useful since weather models mix these constantly)
    public static double hectoPascalsToKiloPascals(double hPa) {
        return hPa / 10.0;
    }

    public static double kiloPascalsToHectoPascals(double kPa) {
        return kPa * 10.0;
    }

    // Pascals ↔ inches of mercury (US weather station classic)
    public static double pascalsToInchesOfMercury(double pa) {
        return pa / 3386.389;
    }

    public static double inchesOfMercuryToPascals(double inHg) {
        return inHg * 3386.389;
    }

    // hPa ↔ inches of mercury (what most people actually want)
    public static double hectoPascalsToInchesOfMercury(double hPa) {
        return pascalsToInchesOfMercury(hectoPascalsToPascals(hPa));
    }

    public static double inchesOfMercuryToHectoPascals(double inHg) {
        return pascalsToHectoPascals(inchesOfMercuryToPascals(inHg));
    }
    /**
     * --- Wind Speed Conversion Helpers ---
     *
     * Base relationships:
     * 1 m/s  = 3.6 km/h
     * 1 mph  = 1.609344 km/h
     * 1 m/s  = 2.236936 mph
     */

    // m/s ↔ km/h
    public static double metersPerSecondToKilometersPerHour(double ms) {
        return ms * 3.6;
    }

    public static double kilometersPerHourToMetersPerSecond(double kph) {
        return kph / 3.6;
    }

    // mph ↔ km/h
    public static double milesPerHourToKilometersPerHour(double mph) {
        return mph * 1.609344;
    }

    public static double kilometersPerHourToMilesPerHour(double kph) {
        return kph / 1.609344;
    }

    // Direct convenience: m/s ↔ mph (you already had one direction)
    public static double metersPerSecondToMilesPerHour(double ms) {
        return ms * 2.236936;
    }

    public static double milesPerHourToMetersPerSecond(double mph) {
        return mph / 2.236936;
    }
    // Pascals ↔ PSI (aviation / engineering crossover)
    public static double pascalsToPsi(double pa) {
        return pa / 6894.757;
    }

    public static double psiToPascals(double psi) {
        return psi * 6894.757;
    }

    // Convenience: hPa ↔ PSI
    public static double hectoPascalsToPsi(double hPa) {
        return pascalsToPsi(hectoPascalsToPascals(hPa));
    }

    public static double psiToHectoPascals(double psi) {
        return pascalsToHectoPascals(psiToPascals(psi));
    }

    /**
     * --- Relative Humidity from Temperature & Dew Point (Magnus Formula) ---
     */
    public static double relativeHumidityFromTempAndDewpoint(double tempC, double dewPtC) {
        final double a = 17.625;
        final double b = 243.04; // °C

        double saturationVaporPressure = Math.exp((a * tempC) / (b + tempC));
        double actualVaporPressure     = Math.exp((a * dewPtC) / (b + dewPtC));

        double rhPercent = 100.0 * (actualVaporPressure / saturationVaporPressure);
        return clamp(rhPercent, 0.0, 100.0);
    }

    /**
     * --- Saturation Vapor Pressure (FAO-56) ---
     * @return kPa
     */
    public static double saturationVaporPressureKpa(double tempC) {
        return 0.6108 * Math.exp((17.27 * tempC) / (tempC + 237.3));
    }

    /**
     * --- Actual Vapor Pressure from Dew Point (FAO-56) ---
     * @return kPa
     */
    public static double actualVaporPressureFromDewPointKpa(double dewPtC) {
        return saturationVaporPressureKpa(dewPtC);
    }

    /**
     * --- Vapor Pressure Deficit (VPD) ---
     * @return kPa
     */
    public static double vaporPressureDeficit(double tempC, double dewPtC) {
        dewPtC = Math.min(dewPtC, tempC);
        double svp = saturationVaporPressureKpa(tempC);
        double avp = actualVaporPressureFromDewPointKpa(dewPtC);
        return Math.max(0.0, svp - avp);
    }

    /**
     * --- Wet-Bulb Temperature (Stull 2011 approximation) ---
     *
     * Inputs: tempC (°C), rhPercent (%)
     * Output: wetBulbC (°C)
     *
     * Good practical approximation for typical surface conditions.
     */
    public static double wetBulbTemperatureC(double tempC, double rhPercent) {
        double rh = clamp(rhPercent, 0.0, 100.0);

        // Stull (2011): Tw ≈ T*atan(0.151977*(RH+8.313659)^(1/2)) + atan(T+RH) - atan(RH-1.676331)
        //              + 0.00391838*RH^(3/2)*atan(0.023101*RH) - 4.686035
        double term1 = tempC * Math.atan(0.151977 * Math.sqrt(rh + 8.313659));
        double term2 = Math.atan(tempC + rh);
        double term3 = -Math.atan(rh - 1.676331);
        double term4 = 0.00391838 * Math.pow(rh, 1.5) * Math.atan(0.023101 * rh);
        return term1 + term2 + term3 + term4 - 4.686035;
    }

    /**
     * --- Heat Index (NOAA Rothfusz Regression) ---
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

        if (R < 13 && T >= 80 && T <= 112) {
            heatIndex -= ((13 - R) / 4.0) * Math.sqrt((17 - Math.abs(T - 95.0)) / 17.0);
        } else if (R > 85 && T >= 80 && T <= 87) {
            heatIndex += ((R - 85) / 10.0) * ((87 - T) / 5.0);
        }

        return heatIndex;
    }

    /**
     * --- Wind Chill (NOAA Formula) ---
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
     * --- Feels-like Temperature (Temp + Dew Point + optional wind) ---
     */
    public static double feelsLikeTemperatureF(double tempF, double dewPointF, Double windMphOrNull) {
        double tempC  = fahrenheitToCelsius(tempF);
        double dewPtC = fahrenheitToCelsius(dewPointF);
        double rh     = relativeHumidityFromTempAndDewpoint(tempC, dewPtC);

        if (windMphOrNull != null) {
            double windMph = windMphOrNull;
            if (tempF <= 50.0 && windMph >= 3.0) {
                return windChillFahrenheit(tempF, windMph);
            }
        }
        return heatIndexFahrenheit(tempF, rh);
    }

    /**
     * --- Growing Degree Days (simple average method) ---
     *
     * baseC: typical 10°C for many crops; apples often use ~4.4°C (40°F) or similar depending on model.
     * Optional upperCutoffC: pass null if you don't want an upper cap.
     *
     * Returns GDD in °C·day for that day.
     */
    public static double growingDegreeDaysC(double tMinC, double tMaxC, double baseC, Double upperCutoffCOrNull) {
        double tMin = tMinC;
        double tMax = tMaxC;

        if (upperCutoffCOrNull != null) {
            double cap = upperCutoffCOrNull;
            tMin = Math.min(tMin, cap);
            tMax = Math.min(tMax, cap);
        }

        double mean = (tMin + tMax) / 2.0;
        return Math.max(0.0, mean - baseC);
    }

    /**
     * --- Chill Hours (simple 0–7.2°C model) ---
     *
     * Returns 1.0 if temp in [0, 7.2], else 0.0.
     * For hourly temperatures, sum these over hours.
     */
    public static double chillHour0to7C(double tempC) {
        return (tempC >= 0.0 && tempC <= 7.2) ? 1.0 : 0.0;
    }

    /**
     * --- Utah Chill Units (hourly) ---
     *
     * Returns chill units for one hour at tempC.
     * Sum over hours for seasonal total.
     *
     * Note: Utah model can be quirky in warm climates; Dynamic Model (chill portions) is more robust,
     * but this is a solid "classic orchard" option.
     */
    public static double utahChillUnitsPerHour(double tempC) {
        // Classic Utah weights (approx)
        if (tempC < 1.4) return 0.0;
        if (tempC < 2.4) return 0.5;
        if (tempC < 9.1) return 1.0;
        if (tempC < 12.4) return 0.5;
        if (tempC < 15.9) return 0.0;
        if (tempC < 18.0) return -0.5;
        return -1.0; // >= 18C
    }

    /**
     * --- Reference Evapotranspiration (ET0), FAO-56 Penman–Monteith (daily) ---
     *
     * This method expects the "meteorology parts" already prepared in consistent units.
     * That keeps it flexible (and avoids a mile-long method that tries to guess everything).
     *
     * Inputs:
     * - netRadiationRn_MJm2day: MJ/m^2/day
     * - soilHeatFluxG_MJm2day: MJ/m^2/day (often ~0 for daily)
     * - tempMeanC: °C
     * - wind2m_ms: m/s (wind speed at 2m height; convert if needed)
     * - saturationVaporPressureEs_kPa: kPa
     * - actualVaporPressureEa_kPa: kPa
     * - slopeDelta_kPaPerC: kPa/°C
     * - psychrometricGamma_kPaPerC: kPa/°C
     *
     * Output:
     * - ET0 mm/day
     */
    public static double et0Fao56PenmanMonteithMmPerDay(
            double netRadiationRn_MJm2day,
            double soilHeatFluxG_MJm2day,
            double tempMeanC,
            double wind2m_ms,
            double saturationVaporPressureEs_kPa,
            double actualVaporPressureEa_kPa,
            double slopeDelta_kPaPerC,
            double psychrometricGamma_kPaPerC
    ) {
        double T = tempMeanC;
        double u2 = Math.max(0.0, wind2m_ms);
        double es = saturationVaporPressureEs_kPa;
        double ea = actualVaporPressureEa_kPa;
        double delta = slopeDelta_kPaPerC;
        double gamma = psychrometricGamma_kPaPerC;

        double vpd = Math.max(0.0, es - ea);

        // FAO-56 form:
        // ET0 = [0.408*Δ*(Rn-G) + γ*(900/(T+273))*u2*(es-ea)] / [Δ + γ*(1+0.34*u2)]
        double numerator = 0.408 * delta * (netRadiationRn_MJm2day - soilHeatFluxG_MJm2day)
                + gamma * (900.0 / (T + 273.0)) * u2 * vpd;

        double denominator = delta + gamma * (1.0 + 0.34 * u2);

        if (denominator <= 0.0) return 0.0;
        return Math.max(0.0, numerator / denominator);
    }

    /**
     * --- Slope of saturation vapor pressure curve (Δ) (FAO-56) ---
     * @return kPa/°C
     */
    public static double slopeDeltaKpaPerC(double tempC) {
        double es = saturationVaporPressureKpa(tempC);
        return (4098.0 * es) / Math.pow(tempC + 237.3, 2.0);
    }

    /**
     * --- Psychrometric constant (γ) (FAO-56) ---
     *
     * Inputs:
     * - pressureKPa: kPa (note: your registry uses hPa for MSLP; convert: kPa = hPa / 10)
     *
     * @return kPa/°C
     */
    public static double psychrometricGammaKpaPerC(double pressureKPa) {
        // gamma = 0.000665 * P
        return 0.000665 * pressureKPa;
    }

    /**
     * --- Leaf Wetness "proxy" for one hour ---
     *
     * Returns 1.0 if conditions likely produce leaf wetness, else 0.0.
     * This is a heuristic, but extremely useful for disease-pressure style tracking.
     *
     * Typical logic:
     * - RH high (>= 90%) OR dew point depression small (T - Td <= 1.5C)
     * - AND low/moderate wind (wind dries leaves)
     * - OR precipitation present
     */
    public static double leafWetnessProxyHour(
            double tempC,
            double dewPtC,
            double rhPercent,
            double windSpeedMs,
            double precipMmThisHour
    ) {
        boolean raining = precipMmThisHour > 0.0;
        double dewDepression = tempC - Math.min(dewPtC, tempC);
        boolean humidOrDewy = (rhPercent >= 90.0) || (dewDepression <= 1.5);
        boolean notTooWindy = windSpeedMs <= 4.5; // heuristic

        return (raining || (humidOrDewy && notTooWindy)) ? 1.0 : 0.0;
    }

    /**
     * --- Frost Risk Index (0..1), heuristic ---
     *
     * Uses wet-bulb and classic radiational-frost conditions (calm + humid + near/freezing).
     * If you have cloud_cover%, feed it in; if not, pass null and it won't factor it.
     */
    public static double frostRiskIndex(
            double tempC,
            double dewPtC,
            Double windSpeedMsOrNull,
            Double cloudCoverPercentOrNull
    ) {
        double rh = relativeHumidityFromTempAndDewpoint(tempC, Math.min(dewPtC, tempC));
        double tw = wetBulbTemperatureC(tempC, rh);

        // Core: wet-bulb near/below 0C is bad news for tender bits.
        double wetBulbRisk = smoothStep(1.5, -1.0, tw); // 0 at 1.5C, 1 at -1C

        // Wind: radiational frosts are worst when calm-ish.
        double wind = (windSpeedMsOrNull == null) ? 1.5 : Math.max(0.0, windSpeedMsOrNull);
        double calmRisk = smoothStep(4.0, 0.5, wind); // 1 when calm, fades by 4 m/s

        // Clouds reduce frost risk (blanket effect)
        double cloudRisk = 1.0;
        if (cloudCoverPercentOrNull != null) {
            double cc = clamp(cloudCoverPercentOrNull, 0.0, 100.0) / 100.0;
            cloudRisk = 1.0 - cc; // clear=1, overcast=0
        }

        double risk = wetBulbRisk * calmRisk * cloudRisk;
        return clamp(risk, 0.0, 1.0);
    }

    /**
     * --- Heat Stress Units: count of "stress hours" style metric for one hour ---
     *
     * Returns:
     * - 1.0 if tempC >= thresholdC AND (optionally) VPD_kPa >= vpdThresholdKPaOrNull
     * - else 0.0
     *
     * This plugs into your "hours above X" framework cleanly.
     */
    public static double heatStressHour(double tempC, double thresholdC, Double vpdKPaOrNull, Double vpdThresholdKPaOrNull) {
        boolean hot = tempC >= thresholdC;
        boolean vpdOk = true;

        if (vpdThresholdKPaOrNull != null) {
            double vpd = (vpdKPaOrNull == null) ? 0.0 : vpdKPaOrNull;
            vpdOk = vpd >= vpdThresholdKPaOrNull;
        }

        return (hot && vpdOk) ? 1.0 : 0.0;
    }

    // -------------------- small helpers --------------------

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    /**
     * Smooth step-ish mapping: returns 0 when x >= high, 1 when x <= low, linear between.
     */
    private static double smoothStep(double high, double low, double x) {
        if (high == low) return (x <= low) ? 1.0 : 0.0;
        double t = (high - x) / (high - low); // x=high => 0, x=low => 1
        return clamp(t, 0.0, 1.0);
    }
}
