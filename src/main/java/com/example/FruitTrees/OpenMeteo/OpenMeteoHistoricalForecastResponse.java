package com.example.FruitTrees.OpenMeteo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoHistoricalForecastResponse{

    public double latitude;
    public double longitude;

    @JsonProperty("generationtime_ms")
    public double generationTimeMs;

    @JsonProperty("utc_offset_seconds")
    public int utcOffsetSeconds;

    public String timezone;

    @JsonProperty("timezone_abbreviation")
    public String timezoneAbbreviation;

    public double elevation;

    @JsonProperty("hourly_units")
    public HourlyUnits hourlyUnits;

    public Hourly hourly;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HourlyUnits {

        public String time;

        @JsonProperty("temperature_2m")
        public String temperature2m;

        @JsonProperty("relative_humidity_2m")
        public String relativeHumidity2m;

        @JsonProperty("dew_point_2m")
        public String dewPoint2m;

        @JsonProperty("apparent_temperature")
        public String apparentTemperature;

        @JsonProperty("precipitation_probability")
        public String precipitationProbability;

        public String precipitation;
        public String rain;
        public String showers;
        public String snowfall;

        @JsonProperty("snow_depth")
        public String snowDepth;

        @JsonProperty("weather_code")
        public String weatherCode;

        @JsonProperty("pressure_msl")
        public String pressureMsl;

        @JsonProperty("surface_pressure")
        public String surfacePressure;

        @JsonProperty("cloud_cover")
        public String cloudCover;

        @JsonProperty("cloud_cover_low")
        public String cloudCoverLow;

        @JsonProperty("cloud_cover_mid")
        public String cloudCoverMid;

        @JsonProperty("cloud_cover_high")
        public String cloudCoverHigh;

        public String visibility;

        public String evapotranspiration;

        @JsonProperty("et0_fao_evapotranspiration")
        public String et0FaoEvapotranspiration;

        @JsonProperty("vapour_pressure_deficit")
        public String vapourPressureDeficit;

        @JsonProperty("wind_speed_10m")
        public String windSpeed10m;

        @JsonProperty("wind_speed_80m")
        public String windSpeed80m;

        @JsonProperty("wind_speed_120m")
        public String windSpeed120m;

        @JsonProperty("wind_speed_180m")
        public String windSpeed180m;

        @JsonProperty("wind_speed_200m")
        public String windSpeed200m;

        @JsonProperty("wind_direction_10m")
        public String windDirection10m;

        @JsonProperty("wind_direction_80m")
        public String windDirection80m;

        @JsonProperty("wind_direction_120m")
        public String windDirection120m;

        @JsonProperty("wind_direction_180m")
        public String windDirection180m;

        @JsonProperty("wind_direction_200m")
        public String windDirection200m;

        @JsonProperty("wind_gusts_10m")
        public String windGusts10m;

        @JsonProperty("temperature_80m")
        public String temperature80m;

        @JsonProperty("temperature_120m")
        public String temperature120m;

        @JsonProperty("temperature_180m")
        public String temperature180m;

        @JsonProperty("soil_temperature_0cm")
        public String soilTemperature0cm;

        @JsonProperty("soil_temperature_6cm")
        public String soilTemperature6cm;

        @JsonProperty("soil_temperature_18cm")
        public String soilTemperature18cm;

        @JsonProperty("soil_temperature_54cm")
        public String soilTemperature54cm;

        @JsonProperty("soil_moisture_0_to_1cm")
        public String soilMoisture0To1cm;

        @JsonProperty("soil_moisture_1_to_3cm")
        public String soilMoisture1To3cm;

        @JsonProperty("soil_moisture_3_to_9cm")
        public String soilMoisture3To9cm;

        @JsonProperty("soil_moisture_9_to_27cm")
        public String soilMoisture9To27cm;

        @JsonProperty("soil_moisture_27_to_81cm")
        public String soilMoisture27To81cm;

        @JsonProperty("uv_index")
        public String uvIndex;

        @JsonProperty("uv_index_clear_sky")
        public String uvIndexClearSky;

        @JsonProperty("is_day")
        public String isDay;

        @JsonProperty("sunshine_duration")
        public String sunshineDuration;

        @JsonProperty("wet_bulb_temperature_2m")
        public String wetBulbTemperature2m;

        @JsonProperty("total_column_integrated_water_vapour")
        public String totalColumnIntegratedWaterVapour;

        public String cape;

        @JsonProperty("lifted_index")
        public String liftedIndex;

        @JsonProperty("convective_inhibition")
        public String convectiveInhibition;

        @JsonProperty("freezing_level_height")
        public String freezingLevelHeight;

        @JsonProperty("boundary_layer_height")
        public String boundaryLayerHeight;

        @JsonProperty("shortwave_radiation")
        public String shortwaveRadiation;

        @JsonProperty("direct_radiation")
        public String directRadiation;

        @JsonProperty("diffuse_radiation")
        public String diffuseRadiation;

        @JsonProperty("direct_normal_irradiance")
        public String directNormalIrradiance;

        @JsonProperty("terrestrial_radiation")
        public String terrestrialRadiation;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hourly {

        public List<String> time;

        @JsonProperty("temperature_2m")
        public List<Double> temperature2m;

        @JsonProperty("relative_humidity_2m")
        public List<Double> relativeHumidity2m;

        @JsonProperty("dew_point_2m")
        public List<Double> dewPoint2m;

        @JsonProperty("apparent_temperature")
        public List<Double> apparentTemperature;

        @JsonProperty("precipitation_probability")
        public List<Double> precipitationProbability;

        public List<Double> precipitation;
        public List<Double> rain;
        public List<Double> showers;
        public List<Double> snowfall;

        @JsonProperty("snow_depth")
        public List<Double> snowDepth;

        @JsonProperty("weather_code")
        public List<Double> weatherCode;

        @JsonProperty("pressure_msl")
        public List<Double> pressureMsl;

        @JsonProperty("surface_pressure")
        public List<Double> surfacePressure;

        @JsonProperty("cloud_cover")
        public List<Double> cloudCover;

        @JsonProperty("cloud_cover_low")
        public List<Double> cloudCoverLow;

        @JsonProperty("cloud_cover_mid")
        public List<Double> cloudCoverMid;

        @JsonProperty("cloud_cover_high")
        public List<Double> cloudCoverHigh;

        public List<Double> visibility;

        public List<Double> evapotranspiration;

        @JsonProperty("et0_fao_evapotranspiration")
        public List<Double> et0FaoEvapotranspiration;

        @JsonProperty("vapour_pressure_deficit")
        public List<Double> vapourPressureDeficit;

        @JsonProperty("wind_speed_10m")
        public List<Double> windSpeed10m;

        @JsonProperty("wind_speed_80m")
        public List<Double> windSpeed80m;

        @JsonProperty("wind_speed_120m")
        public List<Double> windSpeed120m;

        @JsonProperty("wind_speed_180m")
        public List<Double> windSpeed180m;

        @JsonProperty("wind_speed_200m")
        public List<Double> windSpeed200m;

        @JsonProperty("wind_direction_10m")
        public List<Double> windDirection10m;

        @JsonProperty("wind_direction_80m")
        public List<Double> windDirection80m;

        @JsonProperty("wind_direction_120m")
        public List<Double> windDirection120m;

        @JsonProperty("wind_direction_180m")
        public List<Double> windDirection180m;

        @JsonProperty("wind_direction_200m")
        public List<Double> windDirection200m;

        @JsonProperty("wind_gusts_10m")
        public List<Double> windGusts10m;

        @JsonProperty("temperature_80m")
        public List<Double> temperature80m;

        @JsonProperty("temperature_120m")
        public List<Double> temperature120m;

        @JsonProperty("temperature_180m")
        public List<Double> temperature180m;

        @JsonProperty("soil_temperature_0cm")
        public List<Double> soilTemperature0cm;

        @JsonProperty("soil_temperature_6cm")
        public List<Double> soilTemperature6cm;

        @JsonProperty("soil_temperature_18cm")
        public List<Double> soilTemperature18cm;

        @JsonProperty("soil_temperature_54cm")
        public List<Double> soilTemperature54cm;

        @JsonProperty("soil_moisture_0_to_1cm")
        public List<Double> soilMoisture0To1cm;

        @JsonProperty("soil_moisture_1_to_3cm")
        public List<Double> soilMoisture1To3cm;

        @JsonProperty("soil_moisture_3_to_9cm")
        public List<Double> soilMoisture3To9cm;

        @JsonProperty("soil_moisture_9_to_27cm")
        public List<Double> soilMoisture9To27cm;

        @JsonProperty("soil_moisture_27_to_81cm")
        public List<Double> soilMoisture27To81cm;

        @JsonProperty("uv_index")
        public List<Double> uvIndex;

        @JsonProperty("uv_index_clear_sky")
        public List<Double> uvIndexClearSky;

        @JsonProperty("is_day")
        public List<Double> isDay;

        @JsonProperty("sunshine_duration")
        public List<Double> sunshineDuration;

        @JsonProperty("wet_bulb_temperature_2m")
        public List<Double> wetBulbTemperature2m;

        @JsonProperty("total_column_integrated_water_vapour")
        public List<Double> totalColumnIntegratedWaterVapour;

        public List<Double> cape;

        @JsonProperty("lifted_index")
        public List<Double> liftedIndex;

        @JsonProperty("convective_inhibition")
        public List<Double> convectiveInhibition;

        @JsonProperty("freezing_level_height")
        public List<Double> freezingLevelHeight;

        @JsonProperty("boundary_layer_height")
        public List<Double> boundaryLayerHeight;

        @JsonProperty("shortwave_radiation")
        public List<Double> shortwaveRadiation;

        @JsonProperty("direct_radiation")
        public List<Double> directRadiation;

        @JsonProperty("diffuse_radiation")
        public List<Double> diffuseRadiation;

        @JsonProperty("direct_normal_irradiance")
        public List<Double> directNormalIrradiance;

        @JsonProperty("terrestrial_radiation")
        public List<Double> terrestrialRadiation;
    }



}