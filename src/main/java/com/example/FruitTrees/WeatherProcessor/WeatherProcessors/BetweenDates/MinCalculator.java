package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component("Min")
public class MinCalculator extends ProcessWeatherBetweenDates {
    private double finalValue =Double.MAX_VALUE;
    public MinCalculator() {
        super("Min");
    }

    @Override
    protected void onEndDate(String date) {
        int year= DateUtilities.getYear(date);
        super.yearlyDataValues.add(finalValue);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        yearlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(values));
        addProcessedValue(finalValue, year);
            finalValue =Double.MAX_VALUE;
        }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if (value < finalValue) {
            finalValue = value;
        }
    }
}
