package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.Metrics.ConditionType;
import com.example.FruitTrees.Metrics.Period;
import com.example.FruitTrees.Metrics.Unit;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.Metrics.Observation.Values;
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
        outputUnit = Unit.HOUR;
        conditionType= ConditionType.HOURS_WHERE;
        period= Period.YEARLY;

    }
    @Override
    public void before() {
        this.processorName="Hours Above "+ threshold+" Of "+dataType;
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
            addProcessedTextValue(text + " in "+ year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + ": " + hours);
        }
        generateObservation(Values.number(hours));

        hours =0;
    }
    @Override
    protected void processWeatherBetween(double value,  LocalDateTime date) {
        if( value>= super.threshold) {
            hours++;
        }
    }
}
