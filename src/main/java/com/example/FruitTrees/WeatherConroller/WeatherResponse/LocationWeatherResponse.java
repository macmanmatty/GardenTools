package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import com.example.FruitTrees.Location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationWeatherResponse {
        private Map<String, YearlyValues> yearlyWeatherValuesMap = new HashMap<>();
        private Map<String, String> locationTotals =new HashMap<>();
        private Location location;
        private List<String> locationResponses = new ArrayList<>();


    public Map<String, YearlyValues> getYearlyWeatherValuesMap() {
        return yearlyWeatherValuesMap;
    }

    public YearlyValues getYearlyValues(String year){
        YearlyValues yearlyValues=this.yearlyWeatherValuesMap.get(year);
        if(yearlyValues==null){
            yearlyValues=new YearlyValues();
            this.yearlyWeatherValuesMap.put(year, yearlyValues);
        }
        return yearlyValues;

    }

    public void setYearlyWeatherValuesMap(Map<String, YearlyValues> yearlyWeatherValuesMap) {
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

    public void setLocationResponses(List<String> locationResponses) {
        this.locationResponses = locationResponses;
    }
}
