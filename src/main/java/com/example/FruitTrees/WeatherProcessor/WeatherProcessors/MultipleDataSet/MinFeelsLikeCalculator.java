package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MultipleDataSet;

import com.example.FruitTrees.Utilities.WeatherUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  and between dates
 *
 */
@Component("MaxFeelsLike")
@Scope("prototype")

public class MinFeelsLikeCalculator extends ProcessMultipleWeatherDataSetsBetweenDates {
    double feelsLike=Double.MAX_VALUE;
    public MinFeelsLikeCalculator() {
    }
    @Override
    public void before() {

        clearProcessedTextValues();
        yearlyDataValues.clear();
    }

    @Override
    public void calculateMedianAverageValue() {

    }

    @Override
    protected void onEndDate(LocalDateTime localDateTime) {
        int year = localDateTime.getYear();
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text = "Min Feels Like  " + this.feelsLike;
        yearlyValuesResponse.getValues().put(text, String.valueOf(this.feelsLike));
        this.feelsLike = Double.MAX_VALUE;
    }
    @Override
    void processWeatherBetween(List<Double> data, List<String> dataType, LocalDateTime date) {
        double feelsLike= WeatherUtilities.feelsLikeTemperatureF(data.get(0), data.get(1), data.get(2));
        if(feelsLike < this.feelsLike) {
            this.feelsLike = feelsLike;
        }
    }
}
