package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MultipleDataSet;

import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;

import java.util.List;

public abstract class MultipleDataSetWeatherProcessor  extends WeatherProcessor {
    @Override
    public void processWeather(Number value, String date) {
        throw new IllegalArgumentException("");
    }

    /**
     *
     * called for each double in list of doubles for processors that use multiple data sets / types  the data set to process weather value
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */
    public abstract void processWeather(List<Number> value, List<String> dataType, String date);
}
