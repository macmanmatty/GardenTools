package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  and between dates
 *
 */
@Component("LastOccurrenceBelow")
public class LastOccurrenceBelowCalculator extends ProcessWeatherBetweenDates {
    /**
     * the min value
     */
    private double valueToReach;
    private String lastDate;
    public LastOccurrenceBelowCalculator() {
    }
    @Override
    public void before() {
        lastDate =null;
        if(inputParameters.isEmpty()){
            throw new IllegalArgumentException("Parameter");
        }
        this.valueToReach = Double.parseDouble(inputParameters.get(0));
        this.processorName="Last Occurrence Below "+valueToReach+" for "+dataType;
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
        if( value<valueToReach) {
            this.lastDate =date;
        }
    }
}
