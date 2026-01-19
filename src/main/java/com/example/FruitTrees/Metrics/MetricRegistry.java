package com.example.FruitTrees.Metrics;

import java.util.Map;

public final class MetricRegistry {

    private static final Map<String, MetricDef> DEF = Map.ofEntries(

            // Temperature
            Map.entry("temperature_2m", new MetricDef("temperature_2m", QuantityType.TEMPERATURE, "degC")),
            Map.entry("dew_point_2m",   new MetricDef("dew_point_2m",   QuantityType.TEMPERATURE, "degC")),
            Map.entry("wet_bulb_2m",    new MetricDef("wet_bulb_2m",    QuantityType.TEMPERATURE, "degC")),

            // Humidity / vapor
            Map.entry("relative_humidity_2m", new MetricDef("relative_humidity_2m", QuantityType.HUMIDITY, "%")),
            Map.entry("vpd",                  new MetricDef("vpd", QuantityType.VAPOR_PRESSURE, "kPa")),

            // Wind
            Map.entry("wind_speed_10m",     new MetricDef("wind_speed_10m", QuantityType.WIND_SPEED, "m/s")),
            Map.entry("wind_gusts_10m",     new MetricDef("wind_gusts_10m", QuantityType.WIND_SPEED, "m/s")),
            Map.entry("wind_direction_10m", new MetricDef("wind_direction_10m", QuantityType.WIND_DIRECTION, "deg")),

            // Pressure
            Map.entry("pressure_msl", new MetricDef("pressure_msl", QuantityType.PRESSURE, "hPa")),

            // Precip
            Map.entry("rain",        new MetricDef("rain",        QuantityType.PRECIP_DEPTH, "mm")),
            Map.entry("snowfall",    new MetricDef("snowfall",    QuantityType.PRECIP_DEPTH, "mm")),
            Map.entry("precip_rate", new MetricDef("precip_rate", QuantityType.PRECIP_RATE,  "mm/h")),
            Map.entry("snow_depth",  new MetricDef("snow_depth",  QuantityType.LENGTH,       "mm")),

            // Hail
            Map.entry("hail_size",        new MetricDef("hail_size",        QuantityType.LENGTH,     "mm")),
            Map.entry("hail_probability", new MetricDef("hail_probability", QuantityType.PROBABILITY,"%")),

            // Clouds / sky
            Map.entry("cloud_cover", new MetricDef("cloud_cover", QuantityType.PROBABILITY, "%")),
            Map.entry("cloud_base",  new MetricDef("cloud_base",  QuantityType.LENGTH,      "m")),

            // Radiation
            Map.entry("shortwave_radiation", new MetricDef("shortwave_radiation", QuantityType.RADIATION, "W/m2")),
            Map.entry("uv_index",            new MetricDef("uv_index",            QuantityType.RADIATION, "")),

            // Soil
            Map.entry("soil_temperature_10cm", new MetricDef("soil_temperature_10cm", QuantityType.SOIL_TEMPERATURE, "degC")),
            Map.entry("soil_moisture_10cm",    new MetricDef("soil_moisture_10cm",    QuantityType.SOIL_MOISTURE,    "m3/m3")),

            // Plant / ag
            Map.entry("evapotranspiration", new MetricDef("evapotranspiration", QuantityType.EVAPOTRANSPIRATION, "mm")),
            Map.entry("gdd",                new MetricDef("gdd",                QuantityType.TEMPERATURE, "degC*d")),
            Map.entry("chill_hours",        new MetricDef("chill_hours",        QuantityType.DURATION,    "h")),

            // Probability & codes
            Map.entry("precipitation_probability", new MetricDef("precipitation_probability", QuantityType.PROBABILITY, "%")),
            Map.entry("weather_code",               new MetricDef("weather_code",               QuantityType.CODE, ""))
    );

    public static MetricDef defFor(String dataType) {
        return DEF.getOrDefault(dataType, new MetricDef(dataType, QuantityType.OTHER, ""));
    }

    private MetricRegistry() {}
}