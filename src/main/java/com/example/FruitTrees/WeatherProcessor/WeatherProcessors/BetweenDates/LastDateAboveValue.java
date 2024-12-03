package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the last instance  of some
 *  weather value above a certain value  and between dates
 *
 */@Component("LastDateAboveValue")
public class LastDateAboveValue extends ProcessWeatherBetweenDates {
    /**
     * the first value date
     */
    private String date;
    /**
     * the min value
     */
    private double lastValue;


    public LastDateAboveValue() {
    }
    @Override
    public void before() {
        String mode="below";
        if(inputParameters.size()<1){
            throw new IllegalArgumentException("Missing parameter");
        }
        this.lastValue = Double.parseDouble(inputParameters.get(0));
        // set mode to above

        this.processorName="Last Date With Value Above "+mode+" "+inputParameters.get(0);
        values.clear();
        yearlyDataValues.clear();

    }

    @Override
    protected void onEndDate(String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(this.date);
        int year= localDateTime.getYear();
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Last Date With " +dataType+  " Above "+ lastValue;
        yearlyValuesResponse.getValues().put(text, localDateTime.toLocalDate().toString());
        values.add(text+ year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ " was on  "+ localDateTime.toLocalDate().toString()+ " at "+localDateTime.getHour());
        this.date=null;
    }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>= lastValue ) {
            this.date=date;
        }
    }
}
