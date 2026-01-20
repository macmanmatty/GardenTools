package com.example.FruitTrees.Metrics;

import com.example.FruitTrees.Metrics.Unit;

/**
 * Represents a fully specified condition applied to a metric.
 *
 * Example:
 *   HOURS_WHERE dew_point > 74°F
 */
public record Condition(
        ConditionType type,
        Comparison comparison,
        Double threshold,
        Double lowerBound,
        Double upperBound,
        Unit unit
) {

    /**
     * Create a simple single-threshold condition.
     */
    public static Condition threshold(
            ConditionType type,
            Comparison comparison,
            double threshold,
            Unit unit
    ) {
        return new Condition(type, comparison, threshold, null, null, unit);
    }

    /**
     * Create a range condition.
     */
    public static Condition range(
            ConditionType type,
            double lower,
            double upper,
            Unit unit
    ) {
        return new Condition(type, Comparison.BETWEEN, null, lower, upper, unit);
    }

}
