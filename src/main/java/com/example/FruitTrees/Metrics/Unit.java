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

    DEG_C("degC", Dimension.TEMPERATURE, "°C", "degrees Celsius"),
    DEG_F("degF", Dimension.TEMPERATURE, "°F", "degrees Fahrenheit"),
    DEG_K("K",    Dimension.TEMPERATURE, "K",  "kelvin"),
    DEG_C_DAY("degC*d", Dimension.TEMPERATURE, "°C·d", "degree-days (°C·day)"),

    M_PER_S("m/s",  Dimension.WIND_SPEED, "m/s", "meters per second"),
    KM_PER_H("km/h", Dimension.WIND_SPEED, "km/h", "kilometers per hour"),
    MPH("mph",      Dimension.WIND_SPEED, "mph", "miles per hour"),

    // not temp but angle
    DEG("deg", Dimension.ANGLE, "°", "degrees"),

    PA("Pa",   Dimension.PRESSURE, "Pa",   "pascals"),
    HPA("hPa", Dimension.PRESSURE, "hPa",  "hectopascals"),
    KPA("kPa", Dimension.PRESSURE, "kPa",  "kilopascals"),
    INHG("inHg", Dimension.PRESSURE, "inHg", "inches of mercury"),
    PSI("psi", Dimension.PRESSURE, "psi", "pounds per square inch"),

    MM("mm", Dimension.LENGTH, "mm", "millimeters"),
    CM("cm", Dimension.LENGTH, "cm", "centimeters"),
    M("m",   Dimension.LENGTH, "m",  "meters"),
    IN("in", Dimension.LENGTH, "in", "inches"),
    FT("ft", Dimension.LENGTH, "ft", "feet"),

    MM_PER_H("mm/h", Dimension.RATE, "mm/h", "millimeters per hour"),

    W_PER_M2("W/m2", Dimension.RADIATION, "W/m²", "watts per square meter"),
    MJ_PER_M2_DAY("MJ/m2/day", Dimension.RADIATION, "MJ/m²/day", "megajoules per square meter per day"),

    M3_PER_M3("m3/m3", Dimension.VOLUMETRIC_FRACTION, "m³/m³", "cubic meters per cubic meter"),

    PERCENT("%", Dimension.PERCENT, "%", "percent"),

    HOUR("h", Dimension.TIME, "hr", "hours"),
    DAY("d",  Dimension.TIME, "day", "days"),

    DIMENSIONLESS("", Dimension.DIMENSIONLESS, "", "dimensionless"),
    CODE("code", Dimension.CODE, "code", "code"),
    SAME_AS_INPUT("SAI", Dimension.UNKNOWN, "same as input", "same as input"),
    UNKNOWN("?", Dimension.UNKNOWN, "?", "unknown"),
    NONE("", Dimension.UNKNOWN, "", "none"); // see note below

    /**
     * Canonical display / parsing symbol.
     * This is what appears in APIs, registries, and output.
     */
    private final String symbol;

    /** Dimension category used for validation/conversion rules */
    private final Dimension dimension;

    /** Human-friendly short label/symbol for tables, charts, headers */
    private final String pretty;

    /** Human-friendly long name for tooltips / explanations */
    private final String prettyLong;

    Unit(String symbol, Dimension dimension, String pretty, String prettyLong) {
        this.symbol = symbol;
        this.dimension = dimension;
        this.pretty = pretty;
        this.prettyLong = prettyLong;
    }

    /**
     * @return Canonical textual symbol for this unit (e.g. "degC", "m/s", "hPa")
     */
    public String symbol() {
        return symbol;
    }

    public Dimension dimension() {
        return dimension;
    }

    /**
     * @return Human-friendly unit for compact display (e.g. "°F", "mph", "W/m²")
     */
    public String pretty() {
        return pretty;
    }

    /**
     * @return Human-friendly long name (e.g. "degrees Fahrenheit")
     */
    public String prettyLong() {
        return prettyLong;
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
     */
    public static Unit fromCommonName(String s) {
        if (s == null) throw new IllegalArgumentException("Unit string is null");

        String u = s.trim().toLowerCase();

        return switch (u) {

            // ---- Temperature ----
            case "c", "°c", "celsius", "degc" -> DEG_C;
            case "f", "°f", "fahrenheit", "degf" -> DEG_F;
            case "k", "kelvin" -> DEG_K;

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