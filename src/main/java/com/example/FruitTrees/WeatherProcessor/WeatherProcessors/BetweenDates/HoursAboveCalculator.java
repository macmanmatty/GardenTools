package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  and between dates
 *
 */
@Component("HoursAbove")
@Scope("prototype")

public class HoursAboveCalculator extends ProcessWeatherBetweenDates {
    /**
     * the counted hours
     */
    private double hours;

    public HoursAboveCalculator() {
    }
    @Override
    public void before() {
        this.processorName="Hours Above "+ threshold;
        clearProcessedTextValues();
        yearlyDataValues.clear();
    }

    @Override
    public void onEndDate(LocalDateTime date) {
        int year= date.getYear();
        super.yearlyDataValues.add(hours);
       YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Hours Of " +dataType+  " Above "+ threshold;
        yearlyValuesResponse.getValues().put(text, String.valueOf(hours));
        if(!isOnlyCalculateAverage()) {
            addProcessedTextValue(text + year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + ": " + hours);
        }
        hours =0;
    }
    @Override
    protected void processWeatherBetween(Number data, LocalDateTime date) {
        double value=data.doubleValue();
        if( value>= value) {
            hours++;
        }
    }
}
