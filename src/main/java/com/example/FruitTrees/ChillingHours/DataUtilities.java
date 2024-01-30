package com.example.FruitTrees.ChillingHours;

import com.example.FruitTrees.OpenMeteo.OpenMeteoResponse;

import java.util.List;

public class DataUtilities {

    private  DataUtilities() {
    }

    public static List<? extends Number> getHourlyData(OpenMeteoResponse openMeteoResponse, String fieldName) {
        OpenMeteoResponse.HourlyData data=openMeteoResponse.getHourly();
        switch (fieldName) {
            case "temperature2m":
                return data.getTemperature2m();
            case "relativeHumidity2m":
                return data.getRelativeHumidity2m();
            case "dewPoint2m":
                return data.getDewPoint2m();
            case "apparentTemperature":
                return data.getApparentTemperature();
            case "precipitation":
                return data.getPrecipitation();
            case "rain":
                return data.getRain();
            case "snowfall":
                return data.getSnowfall();
            case "snowDepth":
                return data.getSnowDepth();
            case "weatherCode":
                return data.getWeatherCode();
            case "pressureMsl":
                return data.getPressureMsl();
            case "surfacePressure":
                return data.getSurfacePressure();
            case "cloudCover":
                return data.getCloudCover();
            case "cloudCoverLow":
                return data.getCloudCoverLow();
            case "cloudCoverMid":
                return data.getCloudCoverMid();
            case "cloudCoverHigh":
                return data.getCloudCoverHigh();
            case "et0FaoEvapotranspiration":
                return data.getEt0FaoEvapotranspiration();
            case "vapourPressureDeficit":
                return data.getVapourPressureDeficit();
            case "windSpeed10m":
                return data.getWindSpeed10m();
            case "windSpeed100m":
                return data.getWindSpeed100m();
            case "windDirection10m":
                return data.getWindDirection10m();
            case "windDirection100m":
                return data.getWindDirection100m();
            case "windGusts10m":
                return data.getWindGusts10m();
            case "soilTemperature0To7cm":
                return data.getSoilTemperature0To7cm();
            case "soilTemperature7To28cm":
                return data.getSoilTemperature7To28cm();
            case "soilTemperature28To100cm":
                return data.getSoilTemperature28To100cm();
            case "soilTemperature100To255cm":
                return data.getSoilTemperature100To255cm();
            case "soilMoisture0To7cm":
                return data.getSoilMoisture0To7cm();
            case "soilMoisture7To28cm":
                return data.getSoilMoisture7To28cm();
            case "soilMoisture28To100cm":
                return data.getSoilMoisture28To100cm();
            case "soilMoisture100To255cm":
                return data.getSoilMoisture100To255cm();
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}