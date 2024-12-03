package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value below a certain value  and between dates
 *
 */@Component("HoursBelow")
public class HoursBelowCalculator extends ProcessWeatherBetweenDates {
    /**
     * the counted hours
     */
    private double hours;
    /**
     * the min value
     */
    private double maxValue;

    public HoursBelowCalculator() {
    }
    @Override
    public void before() {
        if(inputParameters.size()<1){
            throw new IllegalArgumentException("Missing parameter");
        }
        this.maxValue = Double.parseDouble(inputParameters.get(0));
        this.processorName="hours Below "+inputParameters.get(0);
        values.clear();
        yearlyDataValues.clear();

    }

    @Override
    protected void onEndDate(String date) {
        int year= DateUtilities.getYear(date);
        super.yearlyDataValues.add(hours);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Hours Of " +dataType+  " Below "+maxValue;
        yearlyValuesResponse.getValues().put(text, String.valueOf(hours));
        values.add(text+ year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ hours);
        hours =0;
    }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value<=maxValue) {
            hours++;
        }
    }
}
