package com.example.FruitTrees.Metrics;

/**
 * Canonical physical units used by the weather and agro-climate system.
 *
 * This enum is the single source of truth for:
 *  - display symbols
 *  - parsing from API / user strings
 *  - safe, typed canonicalUnit handling inside the engine
 *
 * Strings are allowed only at the edges; internally everything should use Unit.
 */
public enum Unit {


        DEG_C("degC", Dimension.TEMPERATURE),
        DEG_F("degF", Dimension.TEMPERATURE),
        K("K", Dimension.TEMPERATURE),
        DEG_C_DAY("degC*d", Dimension.TEMPERATURE), // see note below

        M_PER_S("m/s", Dimension.WIND_SPEED),
        KM_PER_H("km/h", Dimension.WIND_SPEED),
        MPH("mph", Dimension.WIND_SPEED),

        DEG("deg", Dimension.ANGLE),

        PA("Pa", Dimension.PRESSURE),
        HPA("hPa", Dimension.PRESSURE),
        KPA("kPa", Dimension.PRESSURE),
        INHG("inHg", Dimension.PRESSURE),
        PSI("psi", Dimension.PRESSURE),

        MM("mm", Dimension.LENGTH),
        CM("cm", Dimension.LENGTH),
        M("m", Dimension.LENGTH),
        IN("in", Dimension.LENGTH),
        FT("ft", Dimension.LENGTH),

        MM_PER_H("mm/h", Dimension.RATE),

        W_PER_M2("W/m2", Dimension.RADIATION),
        MJ_PER_M2_DAY("MJ/m2/day", Dimension.RADIATION),

        M3_PER_M3("m3/m3", Dimension.VOLUMETRIC_FRACTION),

        PERCENT("%", Dimension.PERCENT),

        HOUR("h", Dimension.TIME),
        DAY("d", Dimension.TIME),

        DIMENSIONLESS("", Dimension.DIMENSIONLESS),
        CODE("code", Dimension.CODE),

        UNKNOWN("?", Dimension.UNKNOWN),
        NONE("", Dimension.UNKNOWN); // see note below
    /**
     * Canonical display / parsing symbol.
     * This is what appears in APIs, registries, and output.
     */
    private final String symbol;
    private final Dimension dimension;

    Unit(String symbol, Dimension dimension) {
        this.symbol = symbol;
        this.dimension=dimension;
    }

    /**
     * @return Canonical textual symbol for this canonicalUnit (e.g. "degC", "m/s", "hPa")
     */
    public String symbol() {
        return symbol;
    }
    public Dimension dimension() {return dimension;  }

    /**
     * Parse a canonicalUnit from a string in a safe, case-insensitive, whitespace-tolerant way.
     *
     * @param s Input canonicalUnit string (e.g. " degC ", "MPH", "kPa")
     * @return  Matching Unit enum
     * @throws IllegalArgumentException if the canonicalUnit is unknown
     */
    public static Unit fromString(String s) {
        if (s == null) throw new IllegalArgumentException("Unit string is null");
        String t = s.trim();
        for (Unit u : values()) {
            if (u.symbol.equalsIgnoreCase(t)) {
                return u;
            }
        }
        throw new IllegalArgumentException("Unknown canonicalUnit: " + s);
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

            default -> throw new IllegalArgumentException("Unknown canonicalUnit alias: " + s);
        };
    }
}
