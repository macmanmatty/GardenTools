package com.example.FruitTrees.OpenMeteo;

import java.util.List;

public class OpenMeteoResponse {

    public String latitude;
    public String longitude;
    public String generationTimeMs;
    public int utcOffsetSeconds;
    public String timezone;
    public String timezoneAbbreviation;
    public double elevation;
    public Hourly_Units hourly_Units;
    public Daily_Units daily_Units;

    public Hourly hourly;

    public Daily daily;


    // Getter and setter methods

    // Inner classes for nested structures
    public static class Hourly_Units {
        public String time;
        public String temperature2m;
        public String relativeHumidity2m;
        public String dewPoint2m;
        public String apparentTemperature;
        public String precipitation;
        public String rain;
        public String snowfall;
        public String snowDepth;
        public String weatherCode;
        public String pressureMsl;
        public String surfacePressure;
        public String cloudCover;
        public String cloudCoverLow;
        public String cloudCoverMid;
        public String cloudCoverHigh;
        public String et0FaoEvapotranspiration;
        public String vapourPressureDeficit;
        public String windSpeed10m;
        public String windSpeed100m;
        public String windDirection10m;
        public String windDirection100m;
        public String windGusts10m;
        public String soilTemperature0To7cm;
        public String soilTemperature7To28cm;
        public String soilTemperature28To100cm;
        public String soilTemperature100To255cm;
        public String soilMoisture0To7cm;
        public String soilMoisture7To28cm;
        public String soilMoisture28To100cm;
        public String soilMoisture100To255cm;


    }


    public static class Daily_Units {
        public String time;
        public String weatherCode;
        public String temperature2mMax;
        public String temperature2mMin;
        public String temperature2mMean;
        public String apparentTemperatureMax;
        public String apparentTemperatureMin;
        public String apparentTemperatureMean;
        public String sunrise;
        public String sunset;
        public String daylightDuration;
        public String sunshineDuration;
        public String precipitationSum;
        public String rainSum;
        public String snowfallSum;
        public String precipitationHours;
        public String windSpeed10mMax;
        public String windGusts10mMax;
        public String windDirection10mDominant;
        public String shortwaveRadiationSum;
        public String et0FaoEvapotranspiration;


        // Getter and setter methods
    }


    public static  class Hourly {
        public List<String> time;
        public List<Double> temperature_2m;
        public List<Double> relative_humidity_2m;
        public List<Double> dew_point_2m;
        public List<Double> apparent_temperature;
        public List<Double> precipitation;
        public List<Double> rain;
        public List<Double> snowfall;
        public List<Double> snow_depth;
        public List<Double> weather_code;
        public List<Double> pressure_msl;
        public List<Double> surface_pressure;
        public List<Double> cloud_cover;
        public List<Double> cloud_cover_low;
        public List<Double> cloud_cover_mid;
        public List<Double> cloud_cover_high;
        public List<Double> et0_fao_evapotranspiration;
        public List<Double> vapour_pressure_deficit;
        public List<Double> wind_speed_10m;
        public List<Double> wind_speed_100m;
        public List<Double> wind_direction_10m;
        public List<Double> wind_direction_100m;
        public List<Double> wind_gusts_10m;
        public List<Double> soil_temperature_0_to_7cm;
        public List<Double> soil_temperature_7_to_28cm;
        public List<Double> soil_temperature_28_to_100cm;
        public List<Double> soil_temperature_100_to_255cm;
        public List<Double> soil_moisture_0_to_7cm;
        public List<Double> soil_moisture_7_to_28cm;
        public List<Double> soil_moisture_28_to_100cm;
        public List<Double> soil_moisture_100_to_255cm;

        // Constructors, Getters, and Setters
    }

    public static  class Daily {
        public List<String> time;
        public List<Double> weather_code;
        public List<Double> temperature_2m_max;
        public List<Double> temperature_2m_min;
        public List<Double> temperature_2m_mean;
        public List<Double> apparent_temperature_max;
        public List<Double> apparent_temperature_min;
        public List<Double> apparent_temperature_mean;
        public String sunrise;
        public String sunset;
        public List<Double> daylight_duration;
        public List<Double> sunshine_duration;
        public List<Double> precipitation_sum;
        public List<Double> rain_sum;
        public List<Double> snowfall_sum;
        public List<Double> precipitation_hours;
        public List<Double> wind_speed_10m_max;
        public List<Double> wind_gusts_10m_max;
        public List<Double> wind_direction_10m_dominant;
        public List<Double> shortwave_radiation_sum;
        public List<Double> et0_fao_evapotranspiration;

        // Constructors, Getters, and Setters
    }
}