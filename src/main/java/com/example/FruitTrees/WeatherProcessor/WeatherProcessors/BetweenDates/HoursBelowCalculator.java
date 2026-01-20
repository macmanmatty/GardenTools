package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.Metrics.Observation.Values;
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
        this.processorName="hours Below "+ threshold+" Of "+dataType;
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
        addProcessedTextValue(text + " in "+ year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + ": " + hours);
        generateObservation(Values.number(hours));

        hours =0;
    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if( value<= super.threshold) {
            hours++;
        }
    }
}
