package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("Min")
@Scope("prototype")

public class MinCalculator extends ProcessWeatherBetweenDates {
    private double finalValue =Double.MAX_VALUE;
    public MinCalculator() {
        super("Min");
    }

    @Override
    protected void onEndDate(LocalDateTime date) {
        int year= date.getYear();
        super.yearlyDataValues.add(finalValue);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        yearlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(inputParameters));
        addProcessedTextValue(finalValue, year);
            finalValue =Double.MAX_VALUE;
        }
    @Override
    protected void processWeatherBetween(Number data, LocalDateTime date) {
        double value=data.doubleValue();
        if (value < finalValue) {
            finalValue = value;
        }
    }
}
