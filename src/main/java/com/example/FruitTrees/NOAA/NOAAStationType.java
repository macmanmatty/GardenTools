package com.example.FruitTrees.NOAA;

public enum NOAAStationType {

    DAILY,
    HOURLY,
    FALL_BACK;

    /**
     * Converts a String to a NOAAStationType.
     * Matching is case-insensitive and ignores surrounding whitespace.
     * Examples:
     * "daily"  -> DAILY
     * "DAILY"  -> DAILY
     * " hourly " -> HOURLY
     *
     * @param value String representation of the station type
     * @return matching NOAAStationType
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public static NOAAStationType fromString(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "NOAA station type cannot be null or blank"
            );
        }

        try {
            return NOAAStationType.valueOf(
                    value.trim().toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unknown NOAA station type: " + value,
                    e
            );
        }
    }
}