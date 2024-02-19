package com.example.FruitTrees.ChillingHours.WeatherProcessors.Monthly;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("TotalMonthly")
public class MonthlyTotalCalculator extends MonthlyWeatherProcessor {
    private double finalValue =Double.MIN_VALUE;
    public MonthlyTotalCalculator() {
        super("Monthly Total");

    }
    @Override
    protected void onStartNewMonth(Number value, String date) {

    }

    @Override
    protected void onMonthEnd(Number value, String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        addValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name() );
        finalValue =0;

    }

    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
            finalValue =finalValue+ value;
    }
}
