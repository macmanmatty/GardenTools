package com.example.FruitTrees.ChillingHours;

import java.util.HashMap;
import java.util.Map;

public class YearlyWeather {

   private  Map<String, Double> values = new HashMap<>();

    private String year;

    public Map<String, Double> getValues() {
        return values;
    }

    public void setValues(Map<String, Double> values) {
        this.values = values;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
