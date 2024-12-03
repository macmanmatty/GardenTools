package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the first instance of some
 *  weather value above a certain value  and between dates
 *
 */@Component("FirstDateWithValue")
public class FirstDateAboveValue extends ProcessWeatherBetweenDates {
    /**
     * the first value date
     */
    private String date;
    /**
     * the min value
     */
    private double firstValue;
    public FirstDateAboveValue() {
    }
    @Override
    public void before() {
        if(inputParameters.size()<1){
            throw new IllegalArgumentException("Missing parameter");
        }
        this.firstValue = Double.parseDouble(inputParameters.get(0));
        // set mode to above
        this.processorName="First Date Above "+inputParameters.get(0);
        values.clear();
        yearlyDataValues.clear();

    }
    @Override
    protected void onEndDate(String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(this.date);
        int year= localDateTime.getYear();
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="First instance of " +dataType+  " Above "+ firstValue;
        yearlyValuesResponse.getValues().put(text, localDateTime.toLocalDate().toString());
        values.add(text+ year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ " was on  "+ localDateTime.toLocalDate().toString()+ " at "+localDateTime.getHour());
        this.date=null;
    }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if(value>=firstValue){
            this.date=date;
            terminate();
            onEndDate(date);
        }
    }
}
