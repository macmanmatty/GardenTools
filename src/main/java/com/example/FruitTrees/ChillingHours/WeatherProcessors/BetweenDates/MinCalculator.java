package com.example.FruitTrees.ChillingHours.WeatherProcessors.BetweenDates;
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
    protected void onStartDate(String date) {
    }
    @Override
    protected void onEndDate(String date) {
            LocalDateTime localDateTime=LocalDateTime.parse(date);
            int year=localDateTime.getYear();
        super.yearlyDataValues.add(finalValue);

        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        yearlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(values));

        addValue(finalValue, year);
            finalValue =Double.MAX_VALUE;
        }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if (value < finalValue) {
            finalValue = value;
        }
    }
}
