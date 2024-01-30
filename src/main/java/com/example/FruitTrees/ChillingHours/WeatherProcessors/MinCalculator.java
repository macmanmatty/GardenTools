package com.example.FruitTrees.ChillingHours.WeatherProcessors;

import java.time.LocalDateTime;

public class MinCalculator extends WeatherProcessor {
    private double finalValue =Double.MAX_VALUE;
    boolean counting;

    public MinCalculator() {
        super("Min");
    }

    @Override
    public void processWeather(Number number, String date) {
        double value=number.doubleValue();
        if(dateInRange(date)) {
            if (value < finalValue) {
                finalValue=value;
                counting=true;
            }
        }
        if( counting && isEndDate(date)){
            LocalDateTime localDateTime=LocalDateTime.parse(date);
            addValue(finalValue, localDateTime.getYear() );
            finalValue =Double.MAX_VALUE;
        }
    }


}
