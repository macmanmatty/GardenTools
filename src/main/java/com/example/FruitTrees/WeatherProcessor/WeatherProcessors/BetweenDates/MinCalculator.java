package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.Metrics.Period;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.Metrics.Observation.Values;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("Min")
@Scope("prototype")

public class MinCalculator extends ProcessWeatherBetweenDates {
    private double finalValue =Double.MAX_VALUE;
    public MinCalculator() {
        super("Min");
        period= Period.YEARLY;

    }

    @Override
    protected void onEndDate(LocalDateTime date) {
        int year= date.getYear();
        super.yearlyDataValues.add(finalValue);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        yearlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(threshold));
        addProcessedTextValue(finalValue, year);
        generateObservation(Values.number(finalValue));

        finalValue =Double.MAX_VALUE;
        }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if (value < finalValue) {
            finalValue = value;
        }
    }
}
