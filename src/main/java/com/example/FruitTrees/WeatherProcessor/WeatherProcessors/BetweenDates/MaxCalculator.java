package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.Metrics.Period;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.Metrics.Observation.Values;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("Max")
@Scope("prototype")

public class MaxCalculator extends ProcessWeatherBetweenDates {
    private double finalValue =Double.MIN_VALUE;
    public MaxCalculator() {
        super("Max");
        period= Period.YEARLY;

    }
    @Override
    protected void onEndDate(LocalDateTime date) {
        int year= date.getYear();
            super.yearlyDataValues.add(finalValue);
             addProcessedTextValue(finalValue, year);
            YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
            yearlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(threshold));
            generateObservation(Values.number(finalValue));

        finalValue =Double.MIN_VALUE;

    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if (value > finalValue) {
            finalValue = value;
        }
    }
}
