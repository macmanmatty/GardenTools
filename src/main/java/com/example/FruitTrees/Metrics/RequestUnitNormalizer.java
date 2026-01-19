package com.example.FruitTrees.Metrics;

import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation.NumberValue;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation.Observation;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts user-provided request values (thresholds/bounds) into canonical units
 * defined by MetricRegistry so all downstream processors run unit-clean.
 *
 * Rule:
 *  - Only convert at the system boundary (controller/request parsing) and formating data for display
 *  - Inside the engine, everything is in canonical units. @see MetricRegistry
 */
public final class RequestUnitNormalizer {
    private RequestUnitNormalizer() {}

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
        cfg.setUnit(internalUnit);

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


    /**
     * Convert a canonical/internal value back into the user's requested units.
     *
     * Use this when:
     *  - your processors produce canonical values (e.g. °C, m/s, mm)
     *  - but the API response should be in user-friendly units (e.g. °F, mph, inches)
     *
     * @param weatherRequest request containing user unit preferences
     * @param dataType       metric id (external or internal; DataUtilities will normalize)
     * @param canonicalValue numeric value in canonical units
     * @return value converted to the user's preferred units for that quantity
     */
    public static double toUserUnits(WeatherRequest weatherRequest, String dataType, double canonicalValue) {

        // Convert to canonical internal metric id
        String internalType = DataUtilities.toInternalDatatype(dataType);
        if (internalType == null) {
            // If we can't resolve the metric, safest fallback is "no conversion"
            return canonicalValue;
        }

        MetricDef internalMetric = MetricRegistry.defFor(internalType);
        QuantityType qt = internalMetric.type();
        Unit canonicalUnit = internalMetric.canonicalUnit();

        Unit userUnit = userUnitForQuantity(weatherRequest, qt);

        // If user/canonical are unknown, do not guess; return as-is
        if (userUnit == Unit.UNKNOWN || canonicalUnit == Unit.UNKNOWN) {
            return canonicalValue;
        }

        // No-op if already same unit
        if (userUnit == canonicalUnit) {
            return canonicalValue;
        }

        return Units.convert(qt, canonicalValue, canonicalUnit, userUnit);
    }

    // ... your existing methods remain ...

    /**
     * Convert a list of canonical Observations into user-unit Observations (presentation view).
     *
     * IMPORTANT:
     *  - This does NOT modify the input list.
     *  - This does NOT change your canonical "ledger".
     *  - It returns a new list that is safe to render/export.
     */
    public static List<Observation> toUserUnits(WeatherRequest req, List<Observation> canonical) {
        if (canonical == null || canonical.isEmpty()) return canonical;

        List<Observation> out = new ArrayList<>(canonical.size());
        for (Observation obs : canonical) {
            out.add(toUserUnits(req, obs));
        }
        return out;
    }

    /**
     * Convert one canonical Observation into user units (if numeric + convertible).
     */
    public static Observation toUserUnits(WeatherRequest req, Observation obs) {
        if (req == null || obs == null) return obs;

        // Only convert numeric values
        Double numeric = asDoubleOrNull(obs.value());
        if (numeric == null) {
            return obs;
        }

        // Resolve what this metric "means" and what units it uses internally.
        // NOTE: This requires that obs.metricId() maps to a MetricDef in your registry.
        // If your Observation.metricId values are like "temp.hours_below", you’ll want
        // a registry for those (or a mapping layer). See note below.
        MetricDef def = MetricRegistry.defFor(obs.metricId());
        QuantityType qt = def.type();

        Unit canonicalUnit = def.canonicalUnit();
        Unit userUnit = userUnitForQuantity(req, qt);

        // If we can't convert (unknown or not meaningful), keep as-is.
        if (canonicalUnit == Unit.UNKNOWN || userUnit == Unit.UNKNOWN || canonicalUnit == userUnit) {
            return obs;
        }

        double converted = Units.convert(qt, numeric, canonicalUnit, userUnit);

        // Return a NEW observation with the converted value + updated unit string.
        return new Observation(
                obs.locationId(),
                obs.year(),
                obs.month(),
                obs.metricId(),
                new NumberValue(converted),
                userUnit.symbol(),     // update presentation unit
                obs.stat(),
                obs.period(),
                obs.meta()
        );
    }

    /**
     * Try to extract a double from your Value abstraction.
     * Converts only for numeric types.
     */
    private static Double asDoubleOrNull(Value v) {
        if (v == null) return null;

        // Fast path: your numeric record
        if (v instanceof NumberValue nv) {
            return nv.v();
        }

        // Generic path: anything whose raw() is a Number
        Object raw = v.raw();
        if (raw instanceof Number n) {
            return n.doubleValue();
        }

        return null;
    }




}
