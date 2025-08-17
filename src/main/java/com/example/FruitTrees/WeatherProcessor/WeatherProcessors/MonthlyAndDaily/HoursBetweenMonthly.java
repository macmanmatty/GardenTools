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


    public HoursBetweenMonthly() {
    }
    @Override
    public void before() {
        super.before();
        super.processorName="Hours Between "+super.lowerBound + "And "+super.upperBound + " Monthly";
        clearProcessedTextValues();
    }

    @Override
    protected void onMonthEnd(double value, LocalDateTime date) {
        String text="Monthly Hours Of " +dataType+  " Between "+ lowerBound + " And " + upperBound;
        super.processorName="Hours Above Monthly "+threshold;
        monthlyValuesResponse.getValues().put(text, String.valueOf(hours));
            addProcessedTextValue(text + " For " + currentMonthName + " " + currentYear + " : " + hours);

        monthlyValues.get(currentMonthName).add(hours);
        hours =0;
    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if( value<= upperBound && value>= lowerBound) {
            hours++;
        }
    }
}
