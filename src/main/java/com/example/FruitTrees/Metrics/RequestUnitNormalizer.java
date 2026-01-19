package com.example.FruitTrees.Metrics;

import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;

import java.util.List;

/**
 * Converts user-provided request values (thresholds/bounds) into canonical units
 * defined by MetricRegistry so all downstream processors run unit-clean.
 *
 * Rule:
 *  - Only convert at the system boundary (controller/request parsing).
 *  - Inside the engine, everything is in canonical units.
 */
public final class RequestUnitNormalizer {

    /**
     * Normalize all HourlyWeatherProcessRequest thresholds/bounds in-place.
     *
     * @param weatherRequest top-level request containing unit preferences
     */
    public static void normalizeHourlyConfigsToCanonical(
            WeatherRequest weatherRequest

    ) {
        // get configs to normailze
        List<HourlyWeatherProcessRequest> hourlyConfigs=weatherRequest.hourlyWeatherProcessRequests;
        if (hourlyConfigs == null || hourlyConfigs.isEmpty()) return;

        for (HourlyWeatherProcessRequest cfg : hourlyConfigs) {
            normalizeOne(weatherRequest, cfg);
        }
    }

    /**
     * Normalize one hourly processor config to canonical units in-place.
     */
    private static void normalizeOne(WeatherRequest weatherRequest, HourlyWeatherProcessRequest cfg) {
        if (cfg == null) return;

        // Convert external/request datatype to your canonical internal metric id
        String internalType = DataUtilities.toInternalDatatype(cfg.getHourlyDataType());
        if (internalType == null) return;

        MetricDef internalMetric = MetricRegistry.defFor(internalType);

        QuantityType internalQuantityType = internalMetric.type();
        Unit internalUnit = internalMetric.canonicalUnit();

        // Determine which unit the USER used for values of this quantity type
        Unit userUnit = userUnitForQuantity(weatherRequest, internalQuantityType);

        // If we don't know how to interpret the user's values, fail fast
        if (userUnit == Unit.UNKNOWN || internalUnit == Unit.UNKNOWN) {
            throw new IllegalArgumentException(
                    "Cannot normalize units for " + internalType +
                    " (quantity=" + internalQuantityType + ", userUnit=" + userUnit + ", canonical=" + internalUnit + ")"
            );
        }

        // Lower bound (optional)
        if (cfg.getLowerBound() != null) {
            double v = Units.convert(internalQuantityType, cfg.getLowerBound(), userUnit, internalUnit);
            cfg.setLowerBound(v);
        }

        // Upper bound (optional)
        if (cfg.getUpperBound() != null) {
            double v = Units.convert(internalQuantityType, cfg.getUpperBound(), userUnit, internalUnit);
            cfg.setUpperBound(v);
        }

        // Threshold (optional)
        if (cfg.getThreshold() != null) {
            double v = Units.convert(internalQuantityType, cfg.getThreshold(), userUnit, internalUnit);
            cfg.setThreshold(v);
        }

        // OPTIONAL: If you have bins with numeric edges, normalize those too.
        // (Depends on what your Bin class looks like; see note below.)
    }

    /**
     * Maps a QuantityType to the corresponding top-level unit preference
     * in the user's request.
     *
     * This is the ONLY place that decides "which user unit applies to this quantity".
     */
    private static Unit userUnitForQuantity(WeatherRequest req, QuantityType quantityType) {
        // These parse aliases like "F", "°F", "fahrenheit", "mph", "kph", etc.
        // If you only accept canonical symbols, use Unit.fromString(...) instead.
        return switch (quantityType) {
            case TEMPERATURE, SOIL_TEMPERATURE ->
                    Unit.fromCommonName(req.getTemperatureUnit());

            case WIND_SPEED ->
                    Unit.fromCommonName(req.getWindSpeedUnit());

            case PRESSURE ->
                    Unit.fromCommonName(req.getPressureUnit());

            case PRECIP_DEPTH, EVAPOTRANSPIRATION ->
                    Unit.fromCommonName(req.getPrecipitationUnit());

            // These are effectively fixed / not user-selectable in most apps
            case HUMIDITY, PROBABILITY -> Unit.PERCENT;
            case WIND_DIRECTION -> Unit.DEG;
            case VAPOR_PRESSURE -> Unit.KPA;   // Keep VPD in kPa always (sane)
            case DURATION -> Unit.HOUR;
            case CODE -> Unit.CODE;

            default -> Unit.UNKNOWN;
        };
    }

    private RequestUnitNormalizer() {}
}
