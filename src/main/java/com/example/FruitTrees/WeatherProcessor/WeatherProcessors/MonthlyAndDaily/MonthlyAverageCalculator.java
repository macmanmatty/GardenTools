package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("AverageMonthly")
@Scope("prototype")

public class MonthlyAverageCalculator extends DailyAndMonthlyWeatherProcessor {
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
    protected void onMonthEnd(double value, LocalDateTime localDateTime) {
        monthlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(finalValue));
        finalValue=(finalValue/hours);
        addProcessedTextValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name());
        monthlyValues.get(currentMonthName).add(finalValue);
        finalValue =0;
        hours=0;
    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
            finalValue =finalValue+ value;
            hours++;
    }
}
