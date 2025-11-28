package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value below a certain value  and between dates
 *
 */@Component("HoursBelow")
@Scope("prototype")

public class HoursBelowCalculator extends ProcessWeatherBetweenDates {
    /**
     * the counted hours
     */
    private double hours;

    public HoursBelowCalculator() {
    }
    @Override
    public void before() {
        this.processorName="hours Below "+ threshold+" for "+dataType;
        clearProcessedTextValues();
        yearlyDataValues.clear();

    }

    @Override
    protected void onEndDate(LocalDateTime date) {
        int year= date.getYear();
        super.yearlyDataValues.add(hours);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Hours Of " +dataType+  " Below "+ threshold;
        yearlyValuesResponse.getValues().put(text, String.valueOf(hours));
        addProcessedTextValue(text+ year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ hours);
        hours =0;
    }
    @Override
    protected void processWeatherBetween(Number data, LocalDateTime date) {
        double value=data.doubleValue();
        if( value<= threshold) {
            hours++;
        }
    }
}
