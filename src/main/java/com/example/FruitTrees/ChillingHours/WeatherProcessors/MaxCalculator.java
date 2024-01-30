package com.example.FruitTrees.ChillingHours.WeatherProcessors;

import java.time.LocalDateTime;

public class MaxCalculator extends WeatherProcessor {
    private double finalValue =Double.MIN_VALUE;
    private boolean counting;

    public MaxCalculator() {
        super("Max");
    }

    @Override
    public void processWeather(Number number, String date) {
        double value=number.doubleValue();
        if(dateInRange(date)) {
            if (value > finalValue) {
                counting=true;
                finalValue = value;
            }
        }
        if( counting && isEndDate(date)){
            LocalDateTime localDateTime=LocalDateTime.parse(date);
            addValue(finalValue, localDateTime.getYear() );
            finalValue =Double.MIN_VALUE;
        }
    }


}
