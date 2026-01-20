package com.example.FruitTrees.Metrics;

/**
 * Presentation-layer utility for rendering a Condition in human-readable form.
 *
 * IMPORTANT DESIGN NOTE:
 * ----------------------

 * This formatter is used ONLY when exporting or displaying data
 * (CSV, XLSX, PDF, UI).
 */
public final class ConditionFormatter {

    /**
     * Convert a Condition into a concise, human-readable phrase.
     *
     * Examples:
     *  - "hours where dew point > 74°F"
     *  - "days where temperature ≤ 32°C"
     *  - "values where wind speed between 5–10 m/s"
     *
     * @param condition   the semantic condition applied during computation
     * @param metricLabel human-friendly name of the metric
     *                    (e.g. "Dew Point (2m)", not an internal id)
     * @return readable description suitable for reports and spreadsheets
     */
    public static String toHumanReadable(Condition condition, String metricLabel) {

        // Defensive: no condition means no predicate to describe
        if (condition == null) {
            return "";
        }

        // Convert enum-style names (HOURS_WHERE) into friendly text ("hours where")
        // We keep this logic here rather than in the enum so wording can evolve.
        String typePhrase = condition.type()
                .name()
                .toLowerCase()
                .replace('_', ' ');

        // Handle range conditions (BETWEEN) separately because they
        // use lower + upper bounds instead of a single threshold.
        switch (condition.comparison()) {

            case BETWEEN:
                return typePhrase + " " + metricLabel +
                        " between " +
                        condition.lowerBound() + "–" + condition.upperBound() +
                        condition.unit().symbol();

            // All single-threshold comparisons (>, ≥, <, ≤, =, ≠)
            default:
                return typePhrase + " " + metricLabel +
                        " " + condition.comparison().symbol() +
                        " " + condition.threshold() +
                        condition.unit().symbol();
        }
    }

    /**
     * Utility class – no instances.
     */
    private ConditionFormatter() {}
}
