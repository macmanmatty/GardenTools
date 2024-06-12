package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  and between dates
 *
 */
@Component("FirstOccurrence")
public class FirstOccurrenceCalculator extends ProcessWeatherBetweenDates {
    /**
     * the min value
     */
    private double valueToReach;

    private String mode="above";

    public FirstOccurrenceCalculator() {
    }
    @Override
    public void before() {
        if(inputParameters.size()<2){
            throw new IllegalArgumentException("Parameter");
        }
        this.valueToReach = Double.parseDouble(inputParameters.get(0).trim());
        this.mode=inputParameters.get(1).trim();
        this.processorName="Fist Occurrence "+mode+  valueToReach+" for "+dataType;
        values.clear();
        yearlyDataValues.clear();
    }

    @Override
    protected void onStartDate(String date) {
        super.onStartDate(date);
        processing=true;
    }

    @Override
    protected void onEndDate(String date) {
        if(processing) {
            LocalDateTime localDateTime = LocalDateTime.parse(date);
            int year = localDateTime.getYear();
            YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
            String text = "Value: " + dataType + " " + valueToReach + "  Was Never Reached  For " + year;
            values.add(text);
            yearlyValuesResponse.getValues().put(processorName, text);
        }

    }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if(mode.equalsIgnoreCase("below")) {
            if (value < valueToReach) {
                setValue(date);
            }
        }
        if(mode.equalsIgnoreCase("above")) {
            if (value > valueToReach) {
                setValue(date);
            }
        }
        else{
            if (value == valueToReach) {
                setValue(date);
            }

        }
    }

    private  void setValue(String date){

        LocalDateTime localDateTime=LocalDateTime.parse(date);
        int year= localDateTime.getYear();
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text=processorName+" was on "+localDateTime.toLocalDate()+" for "+year;
        values.add(text );
        yearlyValuesResponse.getValues().put(processorName, text);
        processing=false;
    }
}
