package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.ChillingHours.YearlyWeather;

import java.util.ArrayList;
import java.util.List;

public class WeatherResponse {
  public List<String> responses = new ArrayList<>();

    public List<String> getResponses() {
        return responses;
    }

    public void setResponses(List<String> responses) {
        this.responses = responses;
    }
}
