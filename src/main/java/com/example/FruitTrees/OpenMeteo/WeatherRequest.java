package com.example.FruitTrees.OpenMeteo;

import java.util.ArrayList;
import java.util.List;

public class WeatherRequest {

    private List<String> hourlyDataTypes = new ArrayList<>();
    private List<String> dailyDataTypes = new ArrayList<>();

    /**
     * the list of requests for processing  various open meteo datasets
     */
    private List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests = new ArrayList<>();

    /**
     * the unit of measurement for temperature
     * either Fahrenheit
     * or empty string  for Celsius
     */
    private String temperatureUnit;
    /**
     * the unit of measurement for wind speed
     */
    private String windSpeedUnit;
    /**
     * the unit of measurement for snow and rain and precipitation (both snow and rain)
     */
    private String precipitationUnit;
    /**
     * the time zone to use EST EDT CST etc.
     */
    private String timezone;
    /**
     * the mm-dd-yyyy date to start processing the weather data on
     */
    private String startDate;
    /**
     * the mm-dd-yyyy date to stop processing the weather data on
     */
    private String endDate;

    private String longitude;
    private String latitude;

    public List<String> getHourlyDataTypes() {
        return hourlyDataTypes;
    }

    public void setHourlyDataTypes(List<String> hourlyDataTypes) {
        this.hourlyDataTypes = hourlyDataTypes;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public List<String> getDailyDataTypes() {
        return dailyDataTypes;
    }

    public void setDailyDataTypes(List<String> dailyDataTypes) {
        this.dailyDataTypes = dailyDataTypes;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    public void setWindSpeedUnit(String windSpeedUnit) {
        this.windSpeedUnit = windSpeedUnit;
    }

    public String getPrecipitationUnit() {
        return precipitationUnit;
    }

    public void setPrecipitationUnit(String precipitationUnit) {
        this.precipitationUnit = precipitationUnit;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public List<HourlyWeatherProcessRequest> getHourlyWeatherProcessRequests() {
        return hourlyWeatherProcessRequests;
    }

    public void setHourlyWeatherProcessRequests(List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests) {
        this.hourlyWeatherProcessRequests = hourlyWeatherProcessRequests;
    }
}

