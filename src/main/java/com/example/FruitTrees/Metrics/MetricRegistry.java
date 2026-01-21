package com.example.FruitTrees.Metrics;

import java.util.Map;

/**
 * Central registry of all supported weather and agro-climate metrics.
 *
 * Maps:
 *   external metric id (API / computed name)
 *     -> canonical unit (typed via Unit enum)
 *
 * This is the schema of your climate engine.
 * If a variable is not defined here, it is treated as Unit.UNKNOWN.
 */
public final class MetricRegistry {

    /**
     * Immutable lookup table of metric definitions.
     *
     * Key   = canonical metric id (e.g. "temperature_2m")
     * Value = MetricDef(id, canonicalUnit)
     */
    private static final Map<String, MetricDef> DEF = Map.ofEntries(

            // ---------------- Air temperature family ----------------

            Map.entry("temperature_2m", new MetricDef("temperature_2m", Unit.DEG_C)),
            Map.entry("dew_point_2m",   new MetricDef("dew_point_2m",   Unit.DEG_C)),
            Map.entry("wet_bulb_2m",    new MetricDef("wet_bulb_2m",    Unit.DEG_C)),

            // ---------------- Moisture & vapor ----------------

            Map.entry("relative_humidity_2m", new MetricDef("relative_humidity_2m", Unit.PERCENT)),
            Map.entry("vpd",                  new MetricDef("vpd", Unit.KPA)),

            // ---------------- Wind ----------------

            Map.entry("wind_speed_10m",     new MetricDef("wind_speed_10m", Unit.M_PER_S)),
            Map.entry("wind_gusts_10m",     new MetricDef("wind_gusts_10m", Unit.M_PER_S)),
            Map.entry("wind_direction_10m", new MetricDef("wind_direction_10m", Unit.DEG)),

            // ---------------- Pressure ----------------

            Map.entry("pressure_msl", new MetricDef("pressure_msl", Unit.HPA)),

            // ---------------- Precipitation ----------------

            Map.entry("rain",        new MetricDef("rain",        Unit.MM)),
            Map.entry("snowfall",    new MetricDef("snowfall",    Unit.MM)),
            Map.entry("precip_rate", new MetricDef("precip_rate", Unit.MM_PER_H)),
            Map.entry("snow_depth",  new MetricDef("snow_depth",  Unit.MM)),

            // ---------------- Hail ----------------

            Map.entry("hail_size",        new MetricDef("hail_size",        Unit.MM)),
            Map.entry("hail_probability", new MetricDef("hail_probability", Unit.PERCENT)),

            // ---------------- Clouds / sky ----------------

            Map.entry("cloud_cover", new MetricDef("cloud_cover", Unit.PERCENT)),
            Map.entry("cloud_base",  new MetricDef("cloud_base",  Unit.M)),

            // ---------------- Radiation ----------------

            Map.entry("shortwave_radiation", new MetricDef("shortwave_radiation", Unit.W_PER_M2)),
            Map.entry("uv_index",            new MetricDef("uv_index",            Unit.DIMENSIONLESS)),

            // ---------------- Soil ----------------

            Map.entry("soil_temperature_10cm", new MetricDef("soil_temperature_10cm", Unit.DEG_C)),
            Map.entry("soil_moisture_10cm",    new MetricDef("soil_moisture_10cm",    Unit.M3_PER_M3)),

            // ---------------- Plant / ag derived ----------------

            Map.entry("evapotranspiration", new MetricDef("evapotranspiration", Unit.MM)),
            Map.entry("gdd",                new MetricDef("gdd",                Unit.DEG_C_DAY)),
            Map.entry("chill_hours",        new MetricDef("chill_hours",        Unit.HOUR)),

            // ---------------- Probabilities & codes ----------------

            Map.entry("precipitation_probability", new MetricDef("precipitation_probability", Unit.PERCENT)),
            Map.entry("weather_code",               new MetricDef("weather_code",               Unit.CODE))
    );

    /**
     * Lookup a metric definition by its canonical id.
     * Falls back to Unit.UNKNOWN if not registered.
     */
    public static MetricDef defFor(String metricId) {
        return DEF.getOrDefault(metricId, new MetricDef(metricId, Unit.UNKNOWN));
    }

    private MetricRegistry() {}
}

