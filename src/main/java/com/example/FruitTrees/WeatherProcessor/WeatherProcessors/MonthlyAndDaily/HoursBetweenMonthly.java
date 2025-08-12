package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value between a min and max   value  for each month
 *
 */
@Component("HoursBetweenMonthly")
@Scope("prototype")

public class HoursBetweenMonthly extends DailyAndMonthlyWeatherProcessor {
    /**
     * the counted hours
     */
    private double hours;
    /**
     * the max value
     */
    private double maxValue;
    private double minValue;

    public HoursBetweenMonthly() {
    }
    @Override
    public void before() {
        super.before();
        if(inputParameters.size()<2){
            throw new IllegalArgumentException(" Params Array Size<2 You Must Include  The Maximum and Minimum Values In The Array Of Parameters ");
        }
        super.processorName="Hours Between "+inputParameters.get(0)+ "And "+inputParameters.get(1)+ " Monthly";
        this.maxValue = Double.parseDouble(inputParameters.get(1));
        this.minValue=Double.parseDouble(inputParameters.get(0));
        clearProcessedTextValues();
    }

    @Override
    protected void onMonthEnd(double value, LocalDateTime date) {
        String text="Monthly Hours Of " +dataType+  " Between "+minValue+ " And " + maxValue;
        super.processorName="Hours Above Monthly "+inputParameters.get(0);
        monthlyValuesResponse.getValues().put(text, String.valueOf(hours));
            addProcessedTextValue(text + " For " + currentMonthName + " " + currentYear + " : " + hours);

        monthlyValues.get(currentMonthName).add(hours);
        hours =0;
    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if( value<= maxValue && value>=minValue) {
            hours++;
        }
    }
}
