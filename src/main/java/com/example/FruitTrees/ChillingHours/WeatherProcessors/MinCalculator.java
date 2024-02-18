package com.example.FruitTrees.ChillingHours.WeatherProcessors;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("Min")

public class MinCalculator extends ProcessWeatherBetweenDates {
    private double finalValue =Double.MAX_VALUE;


    public MinCalculator() {
        super("Min");
    }

    @Override
    public void before() {
        values.clear();

    }

    @Override
    protected void onStartDate(String date) {

    }

    @Override
    protected void onEndDate(String date) {
            LocalDateTime localDateTime=LocalDateTime.parse(date);
            addValue(finalValue, localDateTime.getYear() );
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
