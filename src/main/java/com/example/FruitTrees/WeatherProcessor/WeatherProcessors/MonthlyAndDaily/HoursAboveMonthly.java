package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  for each month
 *
 */
@Component("HoursAboveMonthly")
@Scope("prototype")

public class HoursAboveMonthly extends DailyAndMonthlyWeatherProcessor {
    /**
     * the counted hours
     */
    private double hours;

    public HoursAboveMonthly() {
    }
    @Override
    public void before() {
        super.before();
        super.processorName="Hours Above "+ threshold +" Monthly  Of "+dataType;
        clearProcessedTextValues();
    }

    @Override
    protected void onMonthEnd(double value, LocalDateTime date) {
        String text="Monthly Hours Of " +dataType+  " Above "+ this.threshold;
        monthlyValuesResponse.getValues().put(text, String.valueOf(hours));
           addProcessedTextValue(text + " For " + currentMonthName + "  " + currentYear + " : " + hours);

        monthlyValues.get(currentMonthName).add(hours);
        hours =0;
    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if( value>= super.threshold) {
            hours++;
        }
    }
}
