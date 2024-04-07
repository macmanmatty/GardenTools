package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * response class for monthly processed weather data
 */
public class MonthlyValuesResponse {
    /**
     * the name of the response usually the year
     */
  private String name="";
    /**
     * the map of monthly weather values
     * key= month name
     * value= processed data
     */
    private Map<String, String> values= new HashMap<>();
    /**
     * the map of daily weather values
     * key= month name
     * value= DailyValuesResponse
     */
    private Map<String, DailyValuesResponse> dailyWeatherValuesMap =new HashMap<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, String> getValues() {
            return values;
        }

        public void setValues(Map<String, String> values) {
            this.values = values;
        }

    public DailyValuesResponse getYearlyValue(String day){
        DailyValuesResponse dailyValuesResponse =this.dailyWeatherValuesMap.get(day);
        if(dailyValuesResponse ==null){
            dailyValuesResponse =new DailyValuesResponse();
            this.dailyWeatherValuesMap.put(day, dailyValuesResponse);
        }
        return dailyValuesResponse;

    }

    public Map<String, DailyValuesResponse> getDailyWeatherValuesMap() {
        return dailyWeatherValuesMap;
    }

    public void setDailyWeatherValuesMap(Map<String, DailyValuesResponse> dailyWeatherValuesMap) {
        this.dailyWeatherValuesMap = dailyWeatherValuesMap;
    }
}
