package com.example.FruitTrees.Metrics;

/**
 * High-level semantic meaning of a condition.
 *
 * This helps presentation layers explain what was counted or filtered.
 */
public enum ConditionType {

    /** Hours where a condition holds (most common in weather stats) */
    HOURS_WHERE,

    /** Days where a condition holds */
    DAYS_WHERE,

    /** Values filtered by condition (e.g. average temp where X) */
    VALUES_WHERE,

    /** Events counted when a condition occurs */
    EVENTS_WHERE,

    /** Continuous ranges (e.g. between A and B) */
    RANGE
}