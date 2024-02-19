package com.example.FruitTrees.ChillingHours.WeatherProcessors;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("MaxMonthly")
public class MonthlyMaxCalculator extends MonthlyWeatherProcessor {
    private double finalValue =Double.MIN_VALUE;
    public MonthlyMaxCalculator() {
        super("Max");

    }
    @Override
    public void before() {
        values.clear();
    }

    @Override
    protected void onStartNewMonth(Number value, String date) {

    }

    @Override
    protected void onMonthEnd(Number value, String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        addValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name() );
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
