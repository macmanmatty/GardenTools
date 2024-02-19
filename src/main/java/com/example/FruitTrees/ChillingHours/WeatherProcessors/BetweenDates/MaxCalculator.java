package com.example.FruitTrees.ChillingHours.WeatherProcessors.BetweenDates;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component("Max")
public class MaxCalculator extends ProcessWeatherBetweenDates {
    private double finalValue =Double.MIN_VALUE;
    public MaxCalculator() {
        super("Max");
    }
    @Override
    protected void onStartDate(String date) {
    }
    @Override
    protected void onEndDate(String date) {
            LocalDateTime localDateTime=LocalDateTime.parse(date);
            addValue(finalValue, localDateTime.getYear() );
            finalValue =Double.MIN_VALUE;
        }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if (value > finalValue) {
            finalValue = value;
        }
    }
}
