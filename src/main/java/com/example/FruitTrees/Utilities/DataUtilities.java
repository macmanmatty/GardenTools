package com.example.FruitTrees.Utilities;

import com.example.FruitTrees.OpenMeteo.OpenMeteoResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtilities {

    private  DataUtilities() {
    }



    public static List<? extends Number> getHourlyData(OpenMeteoResponse openMeteoResponse, String fieldName) {
        OpenMeteoResponse.Hourly data=openMeteoResponse.hourly;
        return switch (fieldName) {
            case "temperature_2m" -> data.temperature_2m;
            case "relative_humidity_2m" -> data.relative_humidity_2m;
            case "dew_point_2m" -> data.dew_point_2m;
            case "apparent_temperature" -> data.apparent_temperature;
            case "precipitation" -> data.precipitation;
            case "rain" -> data.rain;
            case "snowfall" -> data.snowfall;
            case "snow_depth" -> data.snow_depth;
            case "weather_code" -> data.weather_code;
            case "pressure_msl" -> data.pressure_msl;
            case "surface_pressure" -> data.surface_pressure;
            case "cloud_cover" -> data.cloud_cover;
            case "cloud_cover_low" -> data.cloud_cover_low;
            case "cloud_cover_mid" -> data.cloud_cover_mid;
            case "cloud_cover_high" -> data.cloud_cover_high;
            case "et0_fao_evapotranspiration" -> data.et0_fao_evapotranspiration;
            case "vapour_pressure_deficit" -> data.vapour_pressure_deficit;
            case "wind_speed_10m" -> data.wind_speed_10m;
            case "wind_speed_100m" -> data.wind_speed_100m;
            case "wind_direction_10m" -> data.wind_direction_10m;
            case "wind_direction_100m" -> data.wind_direction_100m;
            case "wind_gusts_10m" -> data.wind_gusts_10m;
            case "soil_moisture_0_to_7cm" -> data.soil_moisture_0_to_7cm;
            case "soil_temperature_0_to_7cm" -> data.soil_temperature_0_to_7cm;
            case "soil_temperature_7_to_28cm" -> data.soil_temperature_7_to_28cm;
            case "soil_moisture_7_to_28cm" -> data.soil_moisture_7_to_28cm;
            case "soil_moisture_28_to_100cm" -> data.soil_moisture_28_to_100cm;
            case "soil_temperature_28_to_100cm" -> data.soil_temperature_28_to_100cm;
            case "soil_moisture_100_to_255cm" -> data.soil_moisture_100_to_255cm;
            case "soil_temperature_100_to_255cm" -> data.soil_temperature_100_to_255cm;
            default -> throw new IllegalArgumentException("Invalid field name: " + fieldName);
        };
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