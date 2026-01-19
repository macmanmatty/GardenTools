package com.example.FruitTrees.Metrics;

/**
 * Canonical physical units used by the weather and agro-climate system.
 *
 * This enum is the single source of truth for:
 *  - display symbols
 *  - parsing from API / user strings
 *  - safe, typed unit handling inside the engine
 *
 * Strings are allowed only at the edges; internally everything should use Unit.
 */
public enum Unit {

    // ---------------- Temperature ----------------

    DEG_C("degC"),        // Degrees Celsius (SI meteorological standard)
    DEG_F("degF"),        // Degrees Fahrenheit (US display standard)
    K("K"),               // Kelvin (absolute temperature, physics formulas)
    DEG_C_DAY("degC*d"),  // Degree-days (e.g., Growing Degree Days)

    // ---------------- Wind Speed ----------------

    M_PER_S("m/s"),       // Meters per second (model / physics standard)
    KM_PER_H("km/h"),     // Kilometers per hour (international display)
    MPH("mph"),           // Miles per hour (US display)

    // ---------------- Angle ----------------

    DEG("deg"),           // Degrees (wind direction, solar angles)

    // ---------------- Pressure ----------------

    PA("Pa"),             // Pascals (SI base unit)
    HPA("hPa"),           // Hectopascals (millibars; weather standard)
    KPA("kPa"),           // Kilopascals (used in VPD, ET, psychrometrics)
    INHG("inHg"),         // Inches of mercury (US barometer)
    PSI("psi"),           // Pounds per square inch (engineering crossover)

    // ---------------- Depth / Length ----------------

    MM("mm"),             // Millimeters (precip, ET, snow water equivalent)
    CM("cm"),             // Centimeters (snow depth, hail size)
    M("m"),               // Meters (cloud base, visibility, height)
    IN("in"),             // Inches (US precip, snow depth)
    FT("ft"),             // Feet (cloud ceiling, aviation)

    // ---------------- Rates ----------------

    MM_PER_H("mm/h"),     // Precipitation rate

    // ---------------- Radiation / Energy ----------------

    W_PER_M2("W/m2"),         // Instantaneous radiation flux
    MJ_PER_M2_DAY("MJ/m2/day"), // Daily integrated solar energy

    // ---------------- Soil / Volumetric ----------------

    M3_PER_M3("m3/m3"),   // Volumetric soil moisture (fraction)

    // ---------------- Humidity / Probability ----------------

    PERCENT("%"),         // Relative humidity, cloud cover, probabilities

    // ---------------- Time ----------------

    HOUR("h"),            // Hours (chill hours, leaf wetness duration)
    DAY("d"),             // Days (aggregation, degree-days denominator)

    // ---------------- Dimensionless / Codes ----------------

    DIMENSIONLESS(""),   // Unitless indices (UV index, stability indices)
    CODE("code"),        // Encoded categorical values (WMO weather codes)

    // ---------------- Fallback ----------------

    UNKNOWN("?");        // Placeholder for unclassified or missing units

    /**
     * Canonical display / parsing symbol.
     * This is what appears in APIs, registries, and output.
     */
    private final String symbol;

    Unit(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return Canonical textual symbol for this unit (e.g. "degC", "m/s", "hPa")
     */
    public String symbol() {
        return symbol;
    }

    /**
     * Parse a unit from a string in a safe, case-insensitive, whitespace-tolerant way.
     *
     * @param s Input unit string (e.g. " degC ", "MPH", "kPa")
     * @return  Matching Unit enum
     * @throws IllegalArgumentException if the unit is unknown
     */
    public static Unit fromString(String s) {
        if (s == null) throw new IllegalArgumentException("Unit string is null");
        String t = s.trim();
        for (Unit u : values()) {
            if (u.symbol.equalsIgnoreCase(t)) {
                return u;
            }
        }
        throw new IllegalArgumentException("Unknown unit: " + s);
    }

    /**
     * Convert common aliases and symbols to canonical Unit.
     * Accepts things like:
     *   "C", "°C", "celsius"  -> DEG_C
     *   "F", "°F", "fahrenheit" -> DEG_F
     *   "mps", "m/sec" -> M_PER_S
     *   "mph", "mi/h"  -> MPH
     *   "mb", "millibar" -> HPA
     *   "inches", "inHg" -> INHG
     *   "percent", "%" -> PERCENT
     */
    public static Unit fromCommonName(String s) {
        if (s == null) throw new IllegalArgumentException("Unit string is null");

        String u = s.trim().toLowerCase();

        return switch (u) {

            // ---- Temperature ----
            case "c", "°c", "celsius", "degc" -> DEG_C;
            case "f", "°f", "fahrenheit", "degf" -> DEG_F;
            case "k", "kelvin" -> K;

            // ---- Wind speed ----
            case "m/s", "mps", "m/sec", "meters per second" -> M_PER_S;
            case "km/h", "kph", "kmph", "kilometers per hour" -> KM_PER_H;
            case "mph", "mi/h", "miles per hour" -> MPH;

            // ---- Pressure ----
            case "pa", "pascal", "pascals" -> PA;
            case "hpa", "mb", "millibar", "millibars" -> HPA;
            case "kpa" -> KPA;
            case "inhg", "in hg", "inches of mercury" -> INHG;
            case "psi" -> PSI;

            // ---- Length / Depth ----
            case "mm", "millimeter", "millimeters" -> MM;
            case "cm", "centimeter", "centimeters" -> CM;
            case "m", "meter", "meters" -> M;
            case "in", "inch", "inches" -> IN;
            case "ft", "foot", "feet" -> FT;

            // ---- Rates ----
            case "mm/h", "mmhr", "mm per hour" -> MM_PER_H;

            // ---- Radiation ----
            case "w/m2", "w per m2", "watts per square meter" -> W_PER_M2;
            case "mj/m2/day", "mj per m2 per day" -> MJ_PER_M2_DAY;

            // ---- Soil / Volumetric ----
            case "m3/m3", "vwc", "volumetric water content" -> M3_PER_M3;

            // ---- Probability / Humidity ----
            case "%", "percent", "percentage" -> PERCENT;

            // ---- Time ----
            case "h", "hr", "hour", "hours" -> HOUR;
            case "d", "day", "days" -> DAY;

            // ---- Dimensionless ----
            case "", "unitless", "dimensionless" -> DIMENSIONLESS;

            // ---- Codes ----
            case "code", "wmo", "weather code" -> CODE;

            default -> throw new IllegalArgumentException("Unknown unit alias: " + s);
        };
    }
}
