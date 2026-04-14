package com.example.FruitTrees.Metrics.Observation;

import com.example.FruitTrees.Metrics.*;

import java.util.Map;

/**
 * A single immutable fact produced by a weather processor.
 *
 * Think of this as one row in a scientific ledger:
 *  - one metric
 *  - for one location
 *  - for one time period
 *  - with one clearly defined meaning
 *
 * This record represents DATA, not presentation.
 * No formatting, no English sentences, no UI assumptions.
 *
 * Everything here is intended to be:
 *  - stable
 *  - machine-readable
 *  - reproducible
 *
 * Human-readable explanations are layered on top elsewhere.
 */
public record Observation(

        /**
         * Stable internal identity of the location this observation belongs to.
         *
         * Typically a UUID, database ID, or other persistent identifier derived
         * from latitude/longitude.
         *
         * Used for joins, caching, comparison across runs, and aggregation.
         */
        String locationId,

        /**
         * The season or calendar year this observation is attributed to.
         *
         * For cross-year seasons (e.g. chill accumulation from Nov–Apr),
         * this represents the "season year" (usually the year in which the
         * season begins).
         *
         * Nullable only for non-temporal or fully aggregated observations.
         */
        Integer year,

        /**
         * Calendar month (1–12) if this is a monthly observation.
         *
         * Null if the observation represents a yearly, seasonal,
         * or non-monthly aggregate.
         */
        Integer month,

        /**
         * Stable, machine-readable identifier of what was measured.
         *
         * Examples:
         *  - "chill.utah"
         *  - "temp.hours_below"
         *  - "dewpoint.mean"
         *
         * This identifier is part of the observation’s identity and
         * must never change, even if display names or labels evolve.
         */
        String metricId,

        String dataType,

        /**
         * The typed value of the observation.
         *
         * Encapsulated so that numeric values, timestamps, text,
         * or other structured data can coexist safely.
         */
        Value value,

        /**
         * Physical canonicalUnit associated with the value.
         *
         * This represents the canonicalUnit of the RESULT (not thresholds).
         * Examples:
         *  - "hours"
         *  - "degC"
         *  - "mm"
         *  - "count"
         */
        Unit canonicalUnit,



        /**
         * Statistical meaning of the value.
         *
         * This answers the question:
         *   "What kind of statistic is this number?"
         *
         * Examples:
         *  - BASE   (raw total)
         *  - MEAN   (average)
         *  - MEDIAN
         *  - MIN
         *  - MAX
         *  - P10, P90
         */
        Stat stat,

        /**
         * Temporal aggregation level represented by this observation.
         *
         * Examples:
         *  - HOURLY
         *  - DAILY
         *  - MONTHLY
         *  - YEARLY
         *  - SEASONAL
         */
        Period period,

        /**
         * Optional logical condition applied to the underlying data
         * before aggregation.
         *
         * This represents a predicate such as:
         *  - hours where temperature > 32°C
         *  - days where dew point ≥ 74°F
         *
         * Null when no explicit condition was applied
         * (e.g. simple monthly max or mean).
         */
        Condition condition,

        /**
         * Optional specification describing HOW the observation was computed.
         *
         * This is used for model-based or non-trivial computations, such as:
         *  - Utah chill accumulation
         *  - Growing degree day models
         *  - Piecewise bin/weight scoring functions
         *
         * This is distinct from {@link Condition}:
         *  - Condition = logical predicate
         *  - ComputationSpec = algorithm/model definition
         */
        ComputationSpec spec,

        /**
         * Additional free-form metadata required to interpret, audit,
         * or reproduce the value.
         *
         * This should contain contextual or auxiliary information that
         * does NOT define the identity of the observation itself.
         *
         * Typical entries:
         *  - window = "11-01..04-01"
         *  - partial = true
         *  - missingHours = 143
         *  - source = "open-meteo"
         *
         * This map is intentionally flexible and extensible, but should
         * not duplicate fields already modeled explicitly above.
         */
        Map<String, Object> meta



) {
    @Override
    public String toString() {
        return "Observation{" +
                "locationId='" + locationId.toString() + '\'' +
                ", year=" + year.toString() +
                ", month=" + month.toString() +
                ", metricId='" + metricId.toString() + '\'' +
                ", dataType='" + dataType.toString() + '\'' +
                ", value=" + value.toString() +
                ", canonicalUnit=" + canonicalUnit .toString()+
                ", stat=" + stat.toString() +
                ", period=" + period.toString() +
                ", condition=" + condition.unit()+
                ", spec=" + spec.toString() +
                ", meta=" + meta.toString() +
                '}';
    }
}
