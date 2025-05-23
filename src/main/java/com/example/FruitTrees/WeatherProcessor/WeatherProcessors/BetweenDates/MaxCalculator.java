package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

@Component("Max")
public class MaxCalculator extends ProcessWeatherBetweenDates {
    private double finalValue =Double.MIN_VALUE;
    public MaxCalculator() {
        super("Max");
    }
    @Override
    protected void onEndDate(String date) {
        int year= DateUtilities.getYear(date);
            super.yearlyDataValues.add(finalValue);
             addProcessedTextValue(finalValue, year);
            YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
            yearlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(inputParameters));
            finalValue =Double.MIN_VALUE;
        }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if (value > finalValue) {
            finalValue = value;
        }
    }
}
