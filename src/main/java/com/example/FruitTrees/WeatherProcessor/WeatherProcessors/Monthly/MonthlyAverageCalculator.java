package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Monthly;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("AverageMonthly")
public class MonthlyAverageCalculator extends MonthlyWeatherProcessor {
    private double finalValue =0;
    private int hours;
    public MonthlyAverageCalculator() {
        super("Monthly Average");
    }
    @Override
    public void before() {
        super.before();
        processorName = "Monthly Average For "+dataType;

    }
   
    @Override
    protected void onMonthEnd(Number value, String date) {
        monthlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(finalValue));
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        finalValue=(finalValue/hours);
        addProcessedTextValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name());
        monthlyValues.get(currentMonthName).add(finalValue);
        finalValue =0;
        hours=0;
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
            finalValue =finalValue+ value;
            hours++;
    }
}
