package com.example.FruitTrees.SoilProcessor;

import com.example.FruitTrees.Location.Coordinate;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SoilRequest {

    /**
     * the hourly data types to retrieve from  the open meteo  API
     */
    public Set<String> soilDataTypes = new HashSet<>();
    /**

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
     * the unit of measurement for snow and rain and precipitation (both snow and rain)
     */
    public String waterUnit ="";
    /**
     * the time zone to use EST EDT CST etc.
     */
    public String timezone="EST";

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

    public Set<String> getSoilDataTypes() {
        return soilDataTypes;
    }

    public void setSoilDataTypes(Set<String> soilDataTypes) {
        this.soilDataTypes = soilDataTypes;
    }


    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }


    public String getWaterUnit() {
        return waterUnit;
    }

    public void setWaterUnit(String waterUnit) {
        this.waterUnit = waterUnit;
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

