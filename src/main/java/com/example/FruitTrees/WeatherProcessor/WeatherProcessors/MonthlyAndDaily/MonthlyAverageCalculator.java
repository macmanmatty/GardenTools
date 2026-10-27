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
        processorName = "Monthly Average For " + dataType;
        finalValue = 0;
        hours = 0;
    }
   
    @Override
    protected void onMonthEnd(double value, LocalDateTime date) {
        double average = hours > 0
                ? finalValue / hours
                : Double.NaN;

        monthlyValuesResponse.getValues().put(
                processorName + " For " + dataType,
                String.valueOf(average)
        );

        addProcessedTextValue(
                average,
                date.getYear(),
                date.getMonth().name()
        );

        monthlyValues.get(currentMonthName).add(average);

        finalValue = 0;
        hours = 0;
    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
            finalValue =finalValue+ value;
            hours++;
    }
}
