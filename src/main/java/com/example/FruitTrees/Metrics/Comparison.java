package com.example.FruitTrees.Metrics;

/**
 * Comparison operators used when evaluating thresholds.
 *
 * These define the logical condition applied to a data series
 * before aggregation (e.g. counting hours, filtering values).
 */
public enum Comparison {

    GT(">",  "greater than"),
    GTE(">=", "greater than or equal to"),
    LT("<",  "less than"),
    LTE("<=", "less than or equal to"),
    EQ("=",  "equal to"),
    NE("!=", "not equal to"),
    BETWEEN("between", "between");

    private final String symbol;
    private final String description;

    Comparison(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

    /** Machine-friendly symbol (>, >=, <, etc.) */
    public String symbol() {
        return symbol;
    }

    /** Human-readable description */
    public String description() {
        return description;
    }

    /**
     * Parse from common strings (gt, >, >=, lte, etc.)
     */
    public static Comparison fromString(String s) {
        if (s == null) return null;
        String t = s.trim().toLowerCase();

        return switch (t) {
            case ">", "gt"  -> GT;
            case ">=", "gte" -> GTE;
            case "<", "lt"  -> LT;
            case "<=", "lte" -> LTE;
            case "=", "eq"  -> EQ;
            case "!=", "ne" -> NE;
            case "between", "range" -> BETWEEN;
            default -> throw new IllegalArgumentException("Unknown comparison: " + s);
        };
    }
}