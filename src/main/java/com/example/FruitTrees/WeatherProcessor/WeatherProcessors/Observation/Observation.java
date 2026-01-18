package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation;
import com.example.FruitTrees.WeatherProcessor.Period;
import com.example.FruitTrees.WeatherProcessor.Stat;

import java.util.Map;

/**
 * A single immutable fact produced by a weather processor.
 *
 * Think of this as one row in a scientific ledger:
 * one metric, for one location, for one time period, with one meaning.
 *
 * This is NOT presentation. No formatting, no sentences, no units baked into text.
 * Just identity, value, and enough metadata to interpret it forever.
 */
public record Observation(

        /**
         * Stable internal identity of the location this observation belongs to.
         * Typically a UUID derived from lat/lon or a persistent database ID.
         * Used for joins, caching, and cross-run comparison.
         */
        String locationId,

        /**
         * The season or calendar year this observation is attributed to.
         * For cross-year seasons (e.g. chill Nov–Apr), this is the season year
         * (usually the year in which the season starts).
         * Nullable only for truly non-temporal aggregates.
         */
        Integer year,

        /**
         * Calendar month (1–12) if this is a monthly observation.
         * Null if this is a yearly or whole-season value.
         */
        Integer month,

        /**
         * Stable machine-readable identifier of what was measured.
         * Examples:
         *  - "chill.utah"
         *  - "temp.hours_below"
         *  - "dewpoint.mean"
         *
         * This never changes even if display names do.
         */
        String metricId,

        /**
         * The typed value of the observation.
         * Encapsulated so numbers, timestamps, and text can coexist cleanly.
         */
        Value value,

        /**
         * Physical unit of the value, if applicable.
         * Examples: "hours", "degC", "mm", "count".
         */
        String unit,

        /**
         * Statistical meaning of the value.
         * Examples: BASE (raw total), MEAN, MEDIAN, MIN, MAX, P10, P90.
         */
        Stat stat,

        /**
         * Temporal aggregation level this observation represents.
         * Examples: HOURLY, DAILY, MONTHLY, YEARLY, SEASONAL.
         */
        Period period,

        /**
         * Free-form metadata required to interpret or reproduce the value.
         * Typical entries:
         *  - threshold = 32.0
         *  - model = "Utah"
         *  - window = "11-01..04-01"
         *  - partial = true
         *  - missingHours = 143
         *
         * This is intentionally flexible and extensible.
         */
        Map<String, Object> meta

) {}