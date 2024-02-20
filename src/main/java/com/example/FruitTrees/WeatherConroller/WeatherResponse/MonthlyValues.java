package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import java.util.HashMap;
import java.util.Map;

public class MonthlyValues {
        private String name="";
        private Map<String, String> values= new HashMap<>();
        private Map<String, DailyValues> dailyWeatherValuesMap =new HashMap<>();

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

    public DailyValues getYearlyValue(String day){
        DailyValues dailyValues=this.dailyWeatherValuesMap.get(day);
        if(dailyValues==null){
            dailyValues=new DailyValues();
            this.dailyWeatherValuesMap.put(day, dailyValues);
        }
        return dailyValues;

    }

    public Map<String, DailyValues> getDailyWeatherValuesMap() {
        return dailyWeatherValuesMap;
    }

    public void setDailyWeatherValuesMap(Map<String, DailyValues> dailyWeatherValuesMap) {
        this.dailyWeatherValuesMap = dailyWeatherValuesMap;
    }
}
