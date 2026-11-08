package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import java.util.*;

/**
 * the base weather response object for an API call to /weatherInfo endpoint
 */
public class WeatherResponse {
    /**
     *
     */
    private List<String> responses = new ArrayList<>();
    private Set<String> warnings = new HashSet<>();

    /**
     *  map of weather responses to  LocationWeatherResponses
     *  key=location name
     *  value= LocationWeatherResponses
     */
    private Map<String,LocationWeatherResponse> locationWeatherResponses= new HashMap<>();
    public List<String> getResponses() {
        return responses;
    }
    public void setResponses(List<String> responses) {
        this.responses = responses;
    }

    public Set<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(Set<String> warnings) {
        this.warnings = warnings;
    }

    public Map<String, LocationWeatherResponse> getLocationWeatherResponses() {
        return locationWeatherResponses;
    }


    public void setLocationWeatherResponses(Map<String, LocationWeatherResponse> locationWeatherResponses) {
        this.locationWeatherResponses = locationWeatherResponses;
    }
}
