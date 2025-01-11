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

    public boolean showStringText;

    /**
     * whether to retrieve the unpopulated location data
     *
     */
    private boolean populateLocationData;
    /**
     * if saving to a file the path to save to
     *
     */
    private String filePath;

    /**
     * whether to save the response to a file
     *
     */
    private boolean saveToFile;
    /**
     * The file format to save to
     * JSON, CSV, or XLS
     *
     */
    private String outputFileType;




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

    public boolean isShowStringText() {
        return showStringText;
    }

    public void setShowStringText(boolean showStringText) {
        this.showStringText = showStringText;
    }

    public boolean isPopulateLocationData() {
        return populateLocationData;
    }

    public void setPopulateLocationData(boolean populateLocationData) {
        this.populateLocationData = populateLocationData;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isSaveToFile() {
        return saveToFile;
    }

    public void setSaveToFile(boolean saveToFile) {
        this.saveToFile = saveToFile;
    }

    public String getOutputFileType() {
        return outputFileType;
    }

    public void setOutputFileType(String outputFileType) {
        this.outputFileType = outputFileType;
    }
}

