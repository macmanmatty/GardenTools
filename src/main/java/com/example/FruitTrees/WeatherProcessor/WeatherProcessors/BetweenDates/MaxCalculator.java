package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("Max")
@Scope("prototype")

public class MaxCalculator extends ProcessWeatherBetweenDates {
    private double finalValue =Double.MIN_VALUE;
    public MaxCalculator() {
        super("Max");
    }
    @Override
    protected void onEndDate(LocalDateTime date) {
        int year= date.getYear();
            super.yearlyDataValues.add(finalValue);
             addProcessedTextValue(finalValue, year);
            YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
            yearlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(threshold));
            finalValue =Double.MIN_VALUE;
        }
    @Override
    protected void processWeatherBetween(Number data, LocalDateTime date) {
        double value=data.doubleValue();
        if (value > finalValue) {
            finalValue = value;
        }
    }
}
