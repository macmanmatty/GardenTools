package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

public class WeatherResponse {
  private List<String> responses = new ArrayList<>();
private List<LocationWeatherResponse> locationWeatherResponses= new ArrayList<>();
    public List<String> getResponses() {
        return responses;
    }
    public void setResponses(List<String> responses) {
        this.responses = responses;
    }



    public List<LocationWeatherResponse> getLocationWeatherResponses() {
        return locationWeatherResponses;
    }

    public void setLocationWeatherResponses(List<LocationWeatherResponse> locationWeatherResponses) {
        this.locationWeatherResponses = locationWeatherResponses;
    }
}
