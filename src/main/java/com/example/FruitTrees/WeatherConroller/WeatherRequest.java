package com.example.FruitTrees.WeatherConroller;

import com.example.FruitTrees.Location.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeatherRequest {

    /**
     * the hourly data types to retrieve from  the open meteo  API
     */
    public Set<String> hourlyDataTypes = new HashSet<>();
    /**
     * the daily data types to retrieve from  the open meteo  API
     */
    public Set<String> dailyDataTypes = new HashSet<>();

    /**
     * the list of requests for processing  various open meteo datasets
     */
    public List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests = new ArrayList<>();

    /**
     * the unit of measurement for temperature
     * either Fahrenheit
     * or empty string  for Celsius
     */
    public String temperatureUnit="";
    /**
     * the unit of measurement for wind speed
     */
    public String windSpeedUnit="";
    /**
     * the unit of measurement for snow and rain and precipitation (both snow and rain)
     */
    public String precipitationUnit="";
    /**
     * the time zone to use EST EDT CST etc.
     */
    public String timezone="EST";
    /**
     * the yyyy-mm-dd date to start processing the weather data on
     */
    public String startDate;
    /**
     * the yyyy-mm-dd date to stop processing the weather data on
     */
    public String endDate;

    /**
     * the locations latitude and longitude
     *
     */
    public String longitude;
    public String latitude;

    public List<Location> locations= new ArrayList<>();

    public Set<String> getHourlyDataTypes() {
        return hourlyDataTypes;
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


    public void setHourlyDataTypes(Set<String> hourlyDataTypes) {
        this.hourlyDataTypes = hourlyDataTypes;
    }

    public Set<String> getDailyDataTypes() {
        return dailyDataTypes;
    }

    public void setDailyDataTypes(Set<String> dailyDataTypes) {
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

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}

