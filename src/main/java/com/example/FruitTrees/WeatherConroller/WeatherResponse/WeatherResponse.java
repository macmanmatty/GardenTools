package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherResponse {
  private List<String> responses = new ArrayList<>();
    private Map<String,LocationWeatherResponse> locationWeatherResponses= new HashMap<>();
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
}
