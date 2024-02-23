package com.example.FruitTrees.ChillingHours.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValues;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  and between dates
 *
 */
@Component("HoursAbove")
public class HoursAboveCalculator extends ProcessWeatherBetweenDates {
    /**
     * the counted hours
     */
    private double hours;
    /**
     * the min value
     */
    private double minValue;
    public HoursAboveCalculator() {
        super("Chill Hours");
    }
    @Override
    public void before() {
        if(inputParameters.size()<1){
            throw new IllegalArgumentException("Parameter");
        }
        this.minValue = Double.parseDouble(inputParameters.get(0));
        values.clear();
    }
    @Override
    protected void onStartDate(String date) {
    }
    @Override
    protected void onEndDate(String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        int year= localDateTime.getYear();
       YearlyValues yearlyValues= locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Hours Of " +dataType+  " Above "+minValue;
        if(inputParameters.size()>1) {
            String requestText = inputParameters.get(1);
            if (requestText != null) {
                text = requestText;
            }
        }
        yearlyValues.getValues().put(text, String.valueOf(hours));
        values.add(text+ +year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ hours);
        hours =0;
    }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>= minValue) {
            hours++;
        }
    }
}
