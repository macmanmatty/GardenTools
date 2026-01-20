package com.example.FruitTrees.Metrics;

import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.Metrics.Observation.NumberValue;
import com.example.FruitTrees.Metrics.Observation.Observation;
import com.example.FruitTrees.Metrics.Observation.Value;

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

        if (cfg.getBins() != null && !cfg.getBins().isEmpty()) {
            List<Bin> converted = new ArrayList<>(cfg.getBins().size());
            for (Bin bin : cfg.getBins()) {
                if (bin == null) continue;

                Double lo = bin.min();
                Double hi = bin.max();

                if (lo != null) lo = Units.convert(internalQuantityType, lo, userUnit, internalUnit);
                if (hi != null) hi = Units.convert(internalQuantityType, hi, userUnit, internalUnit);

                converted.add(new Bin(lo, hi, bin.weight())); // adapt constructor/fields
            }
            cfg.setBins(converted);
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
    public static Unit userUnitForQuantity(WeatherRequest req, QuantityType quantityType) {
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
    public static double toUserUnits(WeatherRequest weatherRequest, String dataType, Unit unit,  QuantityType quantityType,  double canonicalValue) {

        // Convert to canonical internal metric id
        String internalType = DataUtilities.toInternalDatatype(dataType);
        if (internalType == null) {
            // If we can't resolve the metric, safest fallback is "no conversion"
            return canonicalValue;
        }

        MetricDef internalMetric = MetricRegistry.defFor(internalType);
        Unit canonicalUnit = internalMetric.canonicalUnit();

        Unit userUnit = userUnitForQuantity(weatherRequest, quantityType);

        // If user/canonical are unknown, do not guess; return as-is
        if (userUnit == Unit.UNKNOWN || canonicalUnit == Unit.UNKNOWN) {
            return canonicalValue;
        }

        // No-op if already same unit
        if (userUnit == canonicalUnit) {
            return canonicalValue;
        }

        return Units.convert(quantityType, canonicalValue, canonicalUnit, userUnit);
    }


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
     *
     * This is presentation-layer conversion only:
     *  - Observations are stored/processed in canonical units.
     *  - Before export/UI, convert numeric values to the user's preferred units.
     *
     * Non-numeric observations are returned unchanged.
     */
    public static Observation toUserUnits(WeatherRequest weatherRequest, Observation observation) {
        if (weatherRequest == null || observation == null) return observation;

        // Only convert numeric values (NumberValue or Value.raw() instanceof Number)
        Double numeric = asDoubleOrNull(observation.value());
        if (numeric == null) {
            return observation;
        }

        // Determine quantity type so we know which user preference applies (temp vs wind vs pressure, etc.)
        // NOTE: this assumes metricId() can be resolved to a MetricDef.
        // If not, you should add a dedicated registry for observation metricIds.
        MetricDef def = MetricRegistry.defFor(observation.dataType());
        QuantityType qt = def.type();

        // Canonical unit is what the observation is currently stored in.
        // With your new Observation shape, this is the MOST reliable source of truth.
        Unit canonicalUnit = observation.condition().unit();

        // Determine the unit the USER wants to see for this quantity.
        Unit userUnit = userUnitForQuantity(weatherRequest, qt);

        // If we can't convert (unknown, not applicable, or already in desired units), keep as-is.
        if (canonicalUnit == Unit.UNKNOWN || userUnit == Unit.UNKNOWN || canonicalUnit == userUnit) {
            return observation;
        }

        // Convert the numeric value from canonical -> user units
        double convertedValue = Units.convert(qt, numeric, canonicalUnit, userUnit);

        // OPTIONAL (recommended for readability):
        // If this observation carries a Condition whose thresholds are in canonical units,
        // you may also want to convert the Condition bounds so "Condition" displays in user units.
        //
        // Condition convertedCondition = convertConditionToUserUnits(obs.condition(), qt, canonicalUnit, userUnit);
        //
        // If you choose NOT to do that, keep the original condition unchanged.
        Condition convertedCondition =convertConditionToUserUnits(observation.condition(),qt,  canonicalUnit, userUnit);

        // Return a NEW Observation with converted value + updated unit.
        // Everything else stays the same (identity + computation context).
        return new Observation(
                observation.locationId(),
                observation.year(),
                observation.month(),
                observation.metricId(),
                observation.dataType(),
                new NumberValue(convertedValue),
                userUnit.symbol(),                 // Unit enum (not symbol string)
                observation.stat(),
                observation.period(),
                convertedCondition,
                observation.spec(),
                observation.meta()
        );
    }
    /**
     * Convert a Condition's thresholds/bounds from canonical -> user units.
     *
     * This is presentation-only. The engine should keep canonical internally.
     */
    private static Condition convertConditionToUserUnits(
            Condition c,
            QuantityType qt,
            Unit canonicalUnit,
            Unit userUnit
    ) {
        if (c == null) return null;

        // If the condition has no unit (or a unit not matching the observation),
        // you may choose to leave it unchanged to avoid incorrect conversions.
        if (c.unit() == null || c.unit() == Unit.UNKNOWN) {
            return c;
        }

        // Only convert if the condition unit matches the canonical unit we expect.
        // (Prevents converting "hours" as if it were "degC", etc.)
        if (c.unit() != canonicalUnit) {
            return c;
        }

        Double threshold = c.threshold();
        Double lower = c.lowerBound();
        Double upper = c.upperBound();

        if (threshold != null) threshold = Units.convert(qt, threshold, canonicalUnit, userUnit);
        if (lower != null) lower = Units.convert(qt, lower, canonicalUnit, userUnit);
        if (upper != null) upper = Units.convert(qt, upper, canonicalUnit, userUnit);

        return new Condition(
                c.type(),
                c.comparison(),
                threshold,
                lower,
                upper,
                userUnit
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
