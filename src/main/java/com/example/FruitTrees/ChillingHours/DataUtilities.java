package com.example.FruitTrees.ChillingHours;

import com.example.FruitTrees.OpenMeteo.OpenMeteoResponse;

import java.util.List;

public class DataUtilities {

    private  DataUtilities() {
    }

    public static List<? extends Number> getHourlyData(OpenMeteoResponse openMeteoResponse, String fieldName) {
        OpenMeteoResponse.Hourly data=openMeteoResponse.hourly;
        switch (fieldName) {
            case "temperature_2m":
                return data.temperature_2m;
            case "relativeHumidity_2m":
                return data.relative_humidity_2m;
            case "dew_Point_2m":
                return data.dew_point_2m;
            case "apparent_Temperature":
                return data.apparent_temperature;
            case "precipitation":
                return data.precipitation;
            case "rain":
                return data.rain;
            case "snowfall":
                return data.snowfall;
            case "snow_Depth":
                return data.snow_depth;
            case "weather_Code":
                return data.weather_code;
            case "pressure_Msl":
                return data.pressure_msl;
            case "surface_Pressure":
                return data.surface_pressure;
            case "cloud_Cover":
                return data.cloud_cover;
            case "cloud_Cover_Low":
                return data.cloud_cover_low;
            case "cloud_Cover_Mid":
                return data.cloud_cover_mid;
            case "cloud_Cover_High":
                return data.cloud_cover_high;
            case "et0_Fao_Evapotranspiration":
                return data.et0_fao_evapotranspiration;
            case "vapour_Pressure_Deficit":
                return data.vapour_pressure_deficit;
            case "wind_Speed_10m":
                return data.wind_speed_10m;
            case "wind_Speed1_00m":
                return data.wind_speed_100m;
            case "wind_Direction_10m":
                return data.wind_direction_10m;
            case "wind_Direction_100m":
                return data.wind_direction_100m;
            case "wind_Gusts_10m":
                return data.wind_gusts_10m;
            case "soil_moisture_0_to_7cm":
                return data.soil_moisture_0_to_7cm;
            case "soil_temperature_0_to_7cm":
                return data.soil_temperature_0_to_7cm;
            case "soil_temperature_7_to_28cm":
                return data.soil_temperature_7_to_28cm;
            case "soil_moisture_7_to_28cm":
                return data.soil_moisture_7_to_28cm;
            case "soil_moisture_28_to_100cm":
                return data.soil_moisture_28_to_100cm;
            case "soil_temperature_28_to_100cm":
                return data.soil_temperature_28_to_100cm;
            case "soil_moisture_100_to_255cm":
                return data.soil_moisture_100_to_255cm;
            case "soil_temperature_100_to_255cm":
                return data.soil_temperature_100_to_255cm;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}