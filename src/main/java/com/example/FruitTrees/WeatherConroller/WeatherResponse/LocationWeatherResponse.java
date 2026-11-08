package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import com.example.FruitTrees.Location.Location;

import java.util.*;

/**
 * the response for the processed weather data for a passed in location
 */
public class LocationWeatherResponse {
    /**
     * the map of yearly weather responses
     * key= location name
     * value= YearlyValuesResponse
     */
        private Map<String, YearlyValuesResponse> yearlyWeatherValuesMap = new HashMap<>();
    /**
     * the map of yearly weather responses
     * key= location name
     * value= location processed weather data
     */
        private Map<String, String> locationTotals =new HashMap<>();
    /**
     * the location object containing data about the location
     */
        private Location location;
        private List<String> locationResponses = new ArrayList<>();
        private Set<String > warnings = new HashSet<>();


    public Map<String, YearlyValuesResponse> getYearlyWeatherValuesMap() {
        return yearlyWeatherValuesMap;
    }

    public YearlyValuesResponse getYearlyValues(String year){
        YearlyValuesResponse yearlyValuesResponse =this.yearlyWeatherValuesMap.get(year);
        if(yearlyValuesResponse ==null){
            yearlyValuesResponse =new YearlyValuesResponse();
            this.yearlyWeatherValuesMap.put(year, yearlyValuesResponse);
        }
        return yearlyValuesResponse;

    }

    public void setYearlyWeatherValuesMap(Map<String, YearlyValuesResponse> yearlyWeatherValuesMap) {
        this.yearlyWeatherValuesMap = yearlyWeatherValuesMap;
    }

    public Map<String, String> getLocationTotals() {
            return locationTotals;
        }

        public void setLocationTotals(Map<String, String> locationTotals) {
            this.locationTotals = locationTotals;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

    public List<String> getLocationResponses() {
        return locationResponses;
    }

    public Set<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(Set<String> warnings) {
        this.warnings = warnings;
    }

    public void setLocationResponses(List<String> locationResponses) {
        this.locationResponses = locationResponses;
    }
}
