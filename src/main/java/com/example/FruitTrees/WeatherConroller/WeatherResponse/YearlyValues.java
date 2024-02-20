package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import java.util.HashMap;
import java.util.Map;

public class YearlyValues {
        private String name="";
        private Map<String, String> values= new HashMap<>();
        private Map<String, MonthlyValues> monthlyWeatherValuesMap =new HashMap<>();

    public YearlyValues() {

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


    public MonthlyValues getMonthlyValues(String month){
        MonthlyValues monthlyValues=this.monthlyWeatherValuesMap.get(month);
        if(monthlyValues==null){
            monthlyValues=new MonthlyValues();
            this.monthlyWeatherValuesMap.put(month, monthlyValues);
        }
        return monthlyValues;

    }

    public Map<String, MonthlyValues> getMonthlyWeatherValuesMap() {
        return monthlyWeatherValuesMap;
    }

    public void setMonthlyWeatherValuesMap(Map<String, MonthlyValues> monthlyWeatherValuesMap) {
        this.monthlyWeatherValuesMap = monthlyWeatherValuesMap;
    }
}
