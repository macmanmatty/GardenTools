package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  and between dates
 *
 */
@Component("LastOccurrence")
public class LastOccurrenceCalculator extends ProcessWeatherBetweenDates {
    /**
     * the min value
     */
    private double valueToReach;
    private String lastDate;
    private String mode="above";

    public LastOccurrenceCalculator() {
    }
    @Override
    public void before() {
        lastDate =null;
        if(inputParameters.isEmpty()){
            throw new IllegalArgumentException("Parameter");
        }
        this.valueToReach = Double.parseDouble(inputParameters.get(0).trim());
        this.mode=inputParameters.get(1).trim();
        this.processorName="Last Occurrence "+mode+ valueToReach+" for "+dataType;
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
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        int year= localDateTime.getYear();
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text;
        if(this.lastDate ==null) {
            text = "Value: " + dataType + " " + valueToReach + "  Was Never Reached  For " + year;
        }
        else{
            text = processorName + " was on " + LocalDateTime.parse(this.lastDate).toLocalDate();
        }
        values.add(text);
        yearlyValuesResponse.getValues().put(processorName, text);

    }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if(mode.equalsIgnoreCase("above")) {
            if (value > valueToReach) {
                this.lastDate = date;
            }
        }
        else if(mode.equalsIgnoreCase("below")) {
            if (value < valueToReach) {
                this.lastDate = date;
            }
        }
        else  {
            if (value == valueToReach) {
                this.lastDate = date;
            }
        }
    }
}
