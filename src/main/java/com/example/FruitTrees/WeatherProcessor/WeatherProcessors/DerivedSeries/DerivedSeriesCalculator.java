package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries;

import java.util.List;

/**
        * Computes a derived hourly time series (one output value per hour)
 * from one or more input hourly time series.
        *
        * Implementations are stateless and thread-safe. All unit conversion
 * must be handled either at ingestion or explicitly inside computeAt().
        *
        * Typical examples:
        *  - VPD (from temperature + dew point)
 *  - Heat Index (from temperature + humidity)
 *  - Wind Chill (from temperature + wind)
 *  - ET0, Chill Portions, Humidex, etc.
        */
public interface DerivedSeriesCalculator {

    /**
     * @return The series key produced by this calculator (e.g. "vpd_kpa", "feels_like_f").
     *         This must be the same string used by processors as their dataType.
     */
    String outputType();

    /**
     * @return List of input series keys that must be present in seriesByType
     *         for this derived series to be computed.
     *         The order of this list defines the order of values in the
     *         requiredInputs[] array passed to computeAt().
     */
    List<String> requiredInputTypes();

    /**
     * @return List of additional input series keys that may be used if present.
     *         If a series is missing, it will simply not be included in the
     *         optionalInputs[] array passed to computeAt().
     *
     *         This allows graceful degradation (e.g. leaf temperature if available,
     *         otherwise fall back to air temperature; wind if available, otherwise
     *         compute heat index only).
     */
    default List<String> optionalInputTypes() {
        return List.of();
    }

    /**
     * Computes the derived value for a single time step.
     *
     * @param requiredInputs  Values for requiredInputTypes() at this hour,
     *                         in the same order as requiredInputTypes().
     * @param optionalInputs  Values for optionalInputTypes() at this hour,
     *                         in the same order as optionalInputTypes(), but
     *                         containing only those series that actually exist.
     *
     * @return The derived value for this hour.
     */
    double computeAt(double[] requiredInputs, double[] optionalInputs);
}
