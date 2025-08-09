package com.example.FruitTrees.WeatherProcessor.BetweenDates;

import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators.DateValueProcessor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestDateValueProcessor extends DateValueProcessor {
    private final List<String> avgValues = new ArrayList<>();

    public TestDateValueProcessor(String name) {
        super(name);
    }

    @Override
    public void addAverageValue(String value) {
        avgValues.add(value);
    }

    public List<String> getAvgValues() {
        return avgValues;
    }

    @Override
    protected void processWeatherBetween(Number data, LocalDateTime date) {

    }
}