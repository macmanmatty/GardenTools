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
}
