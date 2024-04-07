package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import java.util.HashMap;
import java.util.Map;

public class YearlyValuesResponse {
    private String name="";
     /**
     * the map of yearly or semi yearly  weather values
     * key= year name
     * value= processed data
     */
     private Map<String, String> values= new HashMap<>();
    /**
     * the map of monthly   weather responses
     * key= year name
     * value= MonthlyValuesResponse
     */
    private Map<String, MonthlyValuesResponse> monthlyWeatherValuesMap =new HashMap<>();

    public YearlyValuesResponse() {

    }

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


    public MonthlyValuesResponse getMonthlyValues(String month){
        MonthlyValuesResponse monthlyValuesResponse =this.monthlyWeatherValuesMap.get(month);
        if(monthlyValuesResponse ==null){
            monthlyValuesResponse =new MonthlyValuesResponse();
            this.monthlyWeatherValuesMap.put(month, monthlyValuesResponse);
        }
        return monthlyValuesResponse;

    }

    public Map<String, MonthlyValuesResponse> getMonthlyWeatherValuesMap() {
        return monthlyWeatherValuesMap;
    }

    public void setMonthlyWeatherValuesMap(Map<String, MonthlyValuesResponse> monthlyWeatherValuesMap) {
        this.monthlyWeatherValuesMap = monthlyWeatherValuesMap;
    }
}
