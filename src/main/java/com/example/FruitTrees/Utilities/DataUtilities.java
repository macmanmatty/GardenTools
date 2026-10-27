package com.example.FruitTrees.Utilities;
import com.example.FruitTrees.OpenMeteo.OpenMeteoResponse;

import java.util.*;
import java.util.function.BiConsumer;
public class DataUtilities {
    private  DataUtilities() {
    }
    public static Map<String, double[]> getAllHourlyData(OpenMeteoResponse response) {
        OpenMeteoResponse.Hourly data = response.hourly;
        Map<String, List<? extends Number>> rawData = new HashMap<>();
        BiConsumer<String, List<? extends Number>> putIfNotNull =
                (key, value) -> { if (value != null) rawData.put(key, value); };
        putIfNotNull.accept("temperature_2m", data.temperature_2m);
        putIfNotNull.accept("relative_humidity_2m", data.relative_humidity_2m);
        putIfNotNull.accept("dew_point_2m", data.dew_point_2m);
        putIfNotNull.accept("apparent_temperature", data.apparent_temperature);
        putIfNotNull.accept("precipitation", data.precipitation);
        putIfNotNull.accept("rain", data.rain);
        putIfNotNull.accept("snowfall", data.snowfall);
        putIfNotNull.accept("snow_depth", data.snow_depth);
        putIfNotNull.accept("weather_code", data.weather_code);
        putIfNotNull.accept("pressure_msl", data.pressure_msl);
        putIfNotNull.accept("surface_pressure", data.surface_pressure);
        putIfNotNull.accept("cloud_cover", data.cloud_cover);
        putIfNotNull.accept("cloud_cover_low", data.cloud_cover_low);
        putIfNotNull.accept("cloud_cover_mid", data.cloud_cover_mid);
        putIfNotNull.accept("cloud_cover_high", data.cloud_cover_high);
        putIfNotNull.accept("et0_fao_evapotranspiration", data.et0_fao_evapotranspiration);
        putIfNotNull.accept("vapour_pressure_deficit", data.vapour_pressure_deficit);
        putIfNotNull.accept("wind_speed_10m", data.wind_speed_10m);
        putIfNotNull.accept("wind_speed_100m", data.wind_speed_100m);
        putIfNotNull.accept("wind_direction_10m", data.wind_direction_10m);
        putIfNotNull.accept("wind_direction_100m", data.wind_direction_100m);
        putIfNotNull.accept("wind_gusts_10m", data.wind_gusts_10m);
        putIfNotNull.accept("soil_moisture_0_to_7cm", data.soil_moisture_0_to_7cm);
        putIfNotNull.accept("soil_temperature_0_to_7cm", data.soil_temperature_0_to_7cm);
        putIfNotNull.accept("soil_temperature_7_to_28cm", data.soil_temperature_7_to_28cm);
        putIfNotNull.accept("soil_moisture_7_to_28cm", data.soil_moisture_7_to_28cm);
        putIfNotNull.accept("soil_moisture_28_to_100cm", data.soil_moisture_28_to_100cm);
        putIfNotNull.accept("soil_temperature_28_to_100cm", data.soil_temperature_28_to_100cm);
        putIfNotNull.accept("soil_moisture_100_to_255cm", data.soil_moisture_100_to_255cm);
        putIfNotNull.accept("soil_temperature_100_to_255cm", data.soil_temperature_100_to_255cm);
        Map<String, double[]> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<? extends Number>> entry : rawData.entrySet()) {
            List<? extends Number> list = entry.getValue();
            if (list != null) {
                double[] arr = list.stream()
                        .mapToDouble(Number::doubleValue)
                        .toArray();
                result.put(entry.getKey(), arr);
            }
        }
        return result;
    }
    public static String toNOAADatatype(String field) {
        if (field == null) {
            return null;
        }

        switch (field.trim().toLowerCase(Locale.ROOT)) {

            // Temperature
            case "temperature":
            case "temp":
            case "air_temperature":
            case "t2m":
            case "temperature_2m":
            case "2m_temperature":
            case "tmp":
                return "TMP";

            // Dew Point
            case "dew point":
            case "dewpoint":
            case "dew_point":
            case "dew_point_2m":
            case "dew_point_temperature":
            case "dew":
                return "DEW";

            // Sea-Level Pressure
            case "sea level pressure":
            case "sea_level_pressure":
            case "slp":
                return "SLP";

            // Station Pressure
            case "station pressure":
            case "station_pressure":
            case "stp":
                return "MA1";

            // Visibility
            case "visibility":
            case "vis":
                return "VIS";

            // Wind Speed and Direction
            case "wind":
            case "wind speed":
            case "wind_speed":
            case "wind_speed_10m":
            case "10m_wind_speed":
            case "wind direction":
            case "wind_dir":
            case "wind_direction":
            case "wind_direction_10m":
            case "wnd":
                return "WND";

            // Wind Gusts
            case "gust":
            case "wind gust":
            case "gusts":
            case "wind_gust":
            case "wind_gusts":
            case "wind_gusts_10m":
            case "oc1":
                return "OC1";

            // Ceiling Height
            case "cloud ceiling":
            case "ceiling height":
            case "cloud_ceiling":
            case "clg":
                return "CIG";

            // Cloud Cover
            case "sky cover":
            case "cloud cover":
            case "sky_cover":
            case "cloud_cover":
            case "ga1":
                return "GA1";

            // Precipitation
            case "rain":
            case "precip":
            case "precipitation":
            case "precipitation_1h":
            case "hourly_precip":
            case "aa1":
                return "AA1";

            case "aa2":
                return "AA2";

            case "aa3":
                return "AA3";

            case "aa4":
                return "AA4";

            // Snow Depth
            case "snow":
            case "snow depth":
            case "snow_depth":
            case "aj1":
                return "AJ1";

            // Unknown or unsupported datatype
            default:
                return null;
        }
    }
    /**
    converts a string of commonly used data types to open meteo data type.

     */
    public static String toOpenMeteoDatatype(String field) {
        if (field == null) return null;
        switch (field.trim().toLowerCase()) {
            // Temperature
            case "tmp":
            case "temp":
            case "temperature":
            case "temperature_2m":
            case "2m_temperature":
            case "air_temperature":
            case "t2m":
                return "temperature_2m";
            // Dew point
            case "dew":
            case "dew point":
            case "dewpoint":
            case "dew_point":
            case "dew_point_temperature":
            case "dew_point_2m":
            case "md1": case "md2": case "md3": case "md4": case "md5": case "md6":
                return "dew_point_2m";
            // Apparent temperature
            case "apparent temperature":
            case "apparent_temperature":
            case "feels_like":
                return "apparent_temperature";
            // Precipitation
            case "precip":
            case "precipitation":
            case "p01i": case "p06i":
            case "aa1": case "aa2": case "aa3": case "aa4":
            case "rain_total":
            case "hourly_precip":
                return "precipitation";
            // Rain
            case "rain":
                return "rain";
            // Snow
            case "snow":
            case "snowfall":
                return "snowfall";
            // Snow depth
            case "snow depth":
            case "snow_depth":
            case "sn1": case "sn2":
                return "snow_depth";
            // Weather
            case "weather":
            case "weather_code":
            case "weathercode":
                return "weather_code";
            // Sea-level pressure
            case "mslp":
            case "slp":
            case "sea level pressure":
            case "sea_level_pressure":
                return "pressure_msl";
            // Surface pressure
            case "stp":
            case "station pressure":
            case "surface pressure":
            case "surface_pressure":
                return "surface_pressure";
            // Cloud cover (total + layers)
            case "sky cover":
            case "cloud cover":
            case "cloud_cover":
            case "skc":
                return "cloud_cover";
            case "cloud cover low":
            case "cloud_cover_low":
                return "cloud_cover_low";
            case "cloud cover mid":
            case "cloud_cover_mid":
                return "cloud_cover_mid";
            case "cloud cover high":
            case "cloud_cover_high":
                return "cloud_cover_high";
            // Evapotranspiration
            case "et0":
            case "et0_fao":
            case "et0_fao_evapotranspiration":
            case "evapotranspiration":
                return "et0_fao_evapotranspiration";
            // VPD
            case "vapour pressure deficit":
            case "vapour_pressure_deficit":
            case "vpd":
                return "vapour_pressure_deficit";
            // Wind speed
            case "wind":
            case "wind speed":
            case "wind_speed":
            case "wind_speed_10m":
            case "10m wind":
            case "wds":
            case "wnd":
                return "wind_speed_10m";
            case "wind_speed_100m":
            case "100m wind":
                return "wind_speed_100m";
            // Wind direction
            case "wind direction":
            case "wind_dir":
            case "wind_direction":
            case "wind_direction_10m":
            case "wdf":
                return "wind_direction_10m";
            case "wind_direction_100m":
                return "wind_direction_100m";
            // Gusts
            case "gust":
            case "wind gust":
            case "wind_gust":
            case "wind_gusts":
            case "wind_gusts_10m":
            case "gus":
            case "ga1": case "ga2":
                return "wind_gusts_10m";
            // Soil temperature
            case "soiltemp0to7":
            case "soil_temperature_0_to_7cm":
                return "soil_temperature_0_to_7cm";
            case "soiltemp7to28":
            case "soil_temperature_7_to_28cm":
                return "soil_temperature_7_to_28cm";
            case "soiltemp28to100":
            case "soil_temperature_28_to_100cm":
                return "soil_temperature_28_to_100cm";
            case "soiltemp100to255":
            case "soil_temperature_100_to_255cm":
                return "soil_temperature_100_to_255cm";
            // Soil moisture
            case "soilmoist0to7":
            case "soil_moisture_0_to_7cm":
                return "soil_moisture_0_to_7cm";
            case "soilmoist7to28":
            case "soil_moisture_7_to_28cm":
                return "soil_moisture_7_to_28cm";
            case "soilmoist28to100":
            case "soil_moisture_28_to_100cm":
                return "soil_moisture_28_to_100cm";
            case "soilmoist100to255":
            case "soil_moisture_100_to_255cm":
                return "soil_moisture_100_to_255cm";
            // Relative humidity
            case "humidity":
            case "relative_humidity":
            case "relative humidity":
            case "rh2m":
            case "relative_humidity_2m":
            case "humidity_2m":
                return "relative_humidity_2m";
            default:
                throw new IllegalArgumentException("Invalid field name: " + field);
        }
    }
public static String extractValuesForSoil(String fieldName) {
        return switch (fieldName) {
            case "soil_moisture_0_to_7cm", "soil_temperature_0_to_7cm" -> "0 to 7 cm";
            case "soil_temperature_7_to_28cm", "soil_moisture_7_to_28cm" -> "7 to 28 cm";
            case "soil_moisture_28_to_100cm", "soil_temperature_28_to_100cm" -> "28 to 100 cm";
            case "soil_moisture_100_to_255cm", "soil_temperature_100_to_255cm" -> "100 to 255 cm";
            default -> "Not Soil";
        };
    }
}