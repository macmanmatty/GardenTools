package com.example.FruitTrees.WeatherProcessor.Compare;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;

import java.util.List;

public class CompareWeather {

    public WeatherResponse compareWeather( WeatherResponse weatherResponse){
        List<LocationWeatherResponse> locationWeatherResponseList= weatherResponse.getLocationWeatherResponses().values().stream().toList();
        if(locationWeatherResponseList.size()>2){
            throw new IllegalArgumentException("You May Only Compare Two Locations At a Time");
        }
        LocationWeatherResponse locationWeatherResponse1=locationWeatherResponseList.get(0);
        LocationWeatherResponse locationWeatherResponse2=locationWeatherResponseList.get(1);
        return  weatherResponse;
    }
}
