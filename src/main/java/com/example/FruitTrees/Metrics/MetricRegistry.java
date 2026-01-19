package com.example.FruitTrees.Metrics;

import java.util.Map;

/**
 * Central registry of all supported weather and agro-climate metrics.
 *
 * This maps:
 *   external metric id (API / computed name)
 *       -> physical quantity type
 *       -> canonical unit (typed via Unit enum)
 *
 * This is effectively the schema of your climate engine.
 * If a variable is not defined here, it is treated as UNKNOWN / OTHER.
 */
public final class MetricRegistry {

    /**
     * Immutable lookup table of metric definitions.
     *
     * Key   = canonical metric id (e.g. "temperature_2m")
     * Value = MetricDef(id, physical quantity, canonical unit)
     */
    private static final Map<String, MetricDef> DEF = Map.ofEntries(

            // ---------------- Air temperature family ----------------

            // Standard screen-level air temperature at 2 meters
            Map.entry("temperature_2m", new MetricDef("temperature_2m", QuantityType.TEMPERATURE, Unit.DEG_C)),

            // Dew point temperature at 2 meters (used for RH, VPD, frost, fog)
            Map.entry("dew_point_2m",   new MetricDef("dew_point_2m",   QuantityType.TEMPERATURE, Unit.DEG_C)),

            // Wet-bulb temperature at 2 meters (heat stress, evaporative cooling, frost physics)
            Map.entry("wet_bulb_2m",    new MetricDef("wet_bulb_2m",    QuantityType.TEMPERATURE, Unit.DEG_C)),

            // ---------------- Moisture & vapor ----------------

            // Relative humidity at 2 meters (0–100 %)
            Map.entry("relative_humidity_2m", new MetricDef("relative_humidity_2m", QuantityType.HUMIDITY, Unit.PERCENT)),

            // Vapor Pressure Deficit (kPa) – primary plant stress indicator
            Map.entry("vpd",                  new MetricDef("vpd", QuantityType.VAPOR_PRESSURE, Unit.KPA)),

            // ---------------- Wind ----------------

            // Mean wind speed at 10 meters
            Map.entry("wind_speed_10m",     new MetricDef("wind_speed_10m", QuantityType.WIND_SPEED, Unit.M_PER_S)),

            // Peak wind gust at 10 meters
            Map.entry("wind_gusts_10m",     new MetricDef("wind_gusts_10m", QuantityType.WIND_SPEED, Unit.M_PER_S)),

            // Wind direction in degrees from north
            Map.entry("wind_direction_10m", new MetricDef("wind_direction_10m", QuantityType.WIND_DIRECTION, Unit.DEG)),

            // ---------------- Pressure ----------------

            // Mean sea level pressure (standard meteorological reference)
            Map.entry("pressure_msl", new MetricDef("pressure_msl", QuantityType.PRESSURE, Unit.HPA)),

            // ---------------- Precipitation ----------------

            // Liquid precipitation depth
            Map.entry("rain",        new MetricDef("rain",        QuantityType.PRECIP_DEPTH, Unit.MM)),

            // Solid precipitation water equivalent
            Map.entry("snowfall",    new MetricDef("snowfall",    QuantityType.PRECIP_DEPTH, Unit.MM)),

            // Instantaneous precipitation rate
            Map.entry("precip_rate", new MetricDef("precip_rate", QuantityType.PRECIP_RATE,  Unit.MM_PER_H)),

            // Actual snowpack depth on the ground
            Map.entry("snow_depth",  new MetricDef("snow_depth",  QuantityType.LENGTH,       Unit.MM)),

            // ---------------- Hail ----------------

            // Hailstone diameter
            Map.entry("hail_size",        new MetricDef("hail_size",        QuantityType.LENGTH,      Unit.MM)),

            // Probability of hail occurrence
            Map.entry("hail_probability", new MetricDef("hail_probability", QuantityType.PROBABILITY, Unit.PERCENT)),

            // ---------------- Clouds / sky ----------------

            // Fractional cloud cover
            Map.entry("cloud_cover", new MetricDef("cloud_cover", QuantityType.PROBABILITY, Unit.PERCENT)),

            // Height of cloud base above ground
            Map.entry("cloud_base",  new MetricDef("cloud_base",  QuantityType.LENGTH,      Unit.M)),

            // ---------------- Radiation ----------------

            // Downwelling shortwave solar radiation flux
            Map.entry("shortwave_radiation", new MetricDef("shortwave_radiation", QuantityType.RADIATION, Unit.W_PER_M2)),

            // UV index (dimensionless biological exposure scale)
            Map.entry("uv_index",            new MetricDef("uv_index",            QuantityType.RADIATION, Unit.DIMENSIONLESS)),

            // ---------------- Soil ----------------

            // Soil temperature at 10 cm depth
            Map.entry("soil_temperature_10cm", new MetricDef("soil_temperature_10cm", QuantityType.SOIL_TEMPERATURE, Unit.DEG_C)),

            // Volumetric soil moisture at 10 cm depth
            Map.entry("soil_moisture_10cm",    new MetricDef("soil_moisture_10cm",    QuantityType.SOIL_MOISTURE,    Unit.M3_PER_M3)),

            // ---------------- Plant / ag derived ----------------

            // Reference evapotranspiration (water loss to atmosphere)
            Map.entry("evapotranspiration", new MetricDef("evapotranspiration", QuantityType.EVAPOTRANSPIRATION, Unit.MM)),

            // Growing Degree Days (thermal time accumulation)
            Map.entry("gdd",                new MetricDef("gdd",                QuantityType.TEMPERATURE, Unit.DEG_C_DAY)),

            // Winter chill accumulation in hours
            Map.entry("chill_hours",        new MetricDef("chill_hours",        QuantityType.DURATION,    Unit.HOUR)),

            // ---------------- Probabilities & codes ----------------

            // Probability of measurable precipitation
            Map.entry("precipitation_probability", new MetricDef("precipitation_probability", QuantityType.PROBABILITY, Unit.PERCENT)),

            // Encoded weather condition (e.g., WMO weather code)
            Map.entry("weather_code",               new MetricDef("weather_code",               QuantityType.CODE, Unit.CODE))
    );

    /**
     * Lookup a metric definition by its canonical id.
     * Falls back to QuantityType.OTHER and Unit.UNKNOWN if not registered.
     */
    public static MetricDef defFor(String dataType) {
        return DEF.getOrDefault(
                dataType,
                new MetricDef(dataType, QuantityType.OTHER, Unit.UNKNOWN)
        );
    }

    // Static utility class – not instantiable
    private MetricRegistry() {}
}
