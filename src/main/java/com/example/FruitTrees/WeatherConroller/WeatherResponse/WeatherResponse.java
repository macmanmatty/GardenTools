package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import com.example.FruitTrees.Metrics.Observation.WeatherRunContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the base weather response object for an API call to /weatherInfo endpoint
 */
public class WeatherResponse {
    /**
     *
     */
    private List<String> responses = new ArrayList<>();
    /**
     *  map of weather responses to  LocationWeatherResponses
     *  key=location name
     *  value= LocationWeatherResponses
     */
    private Map<String,LocationWeatherResponse> locationWeatherResponses= new HashMap<>();
    private List<WeatherRunContext> weatherRunContext= new ArrayList<>();
    public List<String> getResponses() {
        return responses;
    }
    public void setResponses(List<String> responses) {
        this.responses = responses;
    }


    public Map<String, LocationWeatherResponse> getLocationWeatherResponses() {
        return locationWeatherResponses;
    }


    public void setLocationWeatherResponses(Map<String, LocationWeatherResponse> locationWeatherResponses) {
        this.locationWeatherResponses = locationWeatherResponses;
    }

    public List<WeatherRunContext> getWeatherRunContext() {
        return weatherRunContext;
    }

    public void setWeatherRunContext(List<WeatherRunContext> weatherRunContext) {
        this.weatherRunContext = weatherRunContext;
    }
}
