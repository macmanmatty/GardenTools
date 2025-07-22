package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

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
    }
    @Override
    public void before() {
        if(inputParameters.isEmpty()){
            throw new IllegalArgumentException("Parameter");
        }
        this.processorName="Hours Above "+inputParameters.get(0);

        this.minValue = Double.parseDouble(inputParameters.get(0));
        clearProcessedTextValues();
        yearlyDataValues.clear();
    }

    @Override
    public void onEndDate(String date) {
        int year= DateUtilities.getYear(date);
        super.yearlyDataValues.add(hours);
       YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Hours Of " +dataType+  " Above "+minValue;
        yearlyValuesResponse.getValues().put(text, String.valueOf(hours));
        if(!isOnlyCalculateAverage()) {
            addProcessedTextValue(text + year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + ": " + hours);
        }
        hours =0;
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>= minValue) {
            hours++;
        }
    }
}
