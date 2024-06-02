package com.example.FruitTrees.Utilities;

import com.example.FruitTrees.OpenMeteo.OpenMeteoResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtilities {

    private  DataUtilities() {
    }

    public static Map<String, String> acsDataFields = new HashMap<>();

    static {
        acsDataFields.put("B01003_001E", "Total Population");
        acsDataFields.put("B01002_001E", "Median Age");
        acsDataFields.put("B01001_001E", "Sex by Age (Total Population)");
        acsDataFields.put("B02001_001E", "Race (Total Population)");
        acsDataFields.put("B03001_001E", "Hispanic or Latino Origin by Race (Total Population)");
        acsDataFields.put("B05001_001E", "Nativity and Citizenship Status in the United States (Total Population)");
        acsDataFields.put("B06009_001E", "Educational Attainment by Age (Total Population)");
        acsDataFields.put("B07013_001E", "Geographical Mobility in the Past Year by Age for Current Residence in the United States");
        acsDataFields.put("B08006_001E", "Sex of Workers by Means of Transportation to Work (Total Population)");
        acsDataFields.put("B19013_001E", "Median Household Income in the Past 12 Months (inflation-adjusted dollars)");
        acsDataFields.put("B19083_001E", "Gini Index of Income Inequality");
        acsDataFields.put("B25003_001E", "Total Housing Units");
        acsDataFields.put("B25001_001E", "Housing Units by Type (Total Housing Units)");
        acsDataFields.put("B25002_001E", "Occupancy Status (Total Housing Units)");
        acsDataFields.put("B25004_001E", "Vacancy Status (Total Housing Units)");
        acsDataFields.put("B19301_001E", "Per Capita Income in the Past 12 Months (inflation-adjusted dollars)");
        acsDataFields.put("B19025_001E", "Mean Household Income in the Past 12 Months (inflation-adjusted dollars)");
        acsDataFields.put("B20002_001E", "Total Earnings in the Past 12 Months (inflation-adjusted dollars)");
        acsDataFields.put("B17020_001E", "Income-to-Poverty Ratio");
        acsDataFields.put("B17001_001E", "Population Below Poverty Level");
        acsDataFields.put("B19313_001E", "Aggregate Income in the Past 12 Months (inflation-adjusted dollars)");

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
            case "wind_speed1_00m" -> data.wind_speed_100m;
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