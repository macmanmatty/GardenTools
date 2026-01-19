package com.example.FruitTrees.Metrics;

public enum Unit {

    // --- Temperature ---
    DEG_C("degC"),
    DEG_F("degF"),
    K("K"),

    // --- Wind Speed ---
    M_PER_S("m/s"),
    KM_PER_H("km/h"),
    MPH("mph"),

    // --- Pressure ---
    PA("Pa"),
    HPA("hPa"),
    KPA("kPa"),
    INHG("inHg"),
    PSI("psi"),

    // --- Depth / Length ---
    MM("mm"),
    IN("in");

    private final String symbol;

    Unit(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }

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
