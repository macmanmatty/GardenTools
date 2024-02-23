package com.example.FruitTrees.ChillingHours.WeatherProcessors.Monthly;

import com.example.FruitTrees.ChillingHours.WeatherProcessors.BetweenDates.ProcessWeatherBetweenDates;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValues;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  for each month
 *
 */
@Component("HoursAboveMonthly")
public class HoursAboveMonthly extends MonthlyWeatherProcessor {
    /**
     * the counted hours
     */
    private double hours;
    /**
     * the min value
     */
    private double minValue;
    public HoursAboveMonthly() {
        super("Chill Hours");
    }
    @Override
    public void before() {
        super.before();
        if(inputParameters.size()<1){
            throw new IllegalArgumentException("Parameter");
        }
        this.minValue = Double.parseDouble(inputParameters.get(0));
        values.clear();
    }

    @Override
    protected void onMonthEnd(Number value, String date) {
        String text="Monthly Hours Of " +dataType+  " Above "+minValue;
        if(inputParameters.size()>1) {
            String requestText = inputParameters.get(1);
            if (requestText != null) {
                text = requestText;
            }
        }
        currenMonthlyValues.getValues().put(text, String.valueOf(hours));
        values.add(text+ " For "+ currentMonthName+"  "+currentYear+" : "+ hours);
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
