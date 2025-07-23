package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 *  A weather processor that calculates the last instance  of some
 *  weather value below a certain value  and between dates
 *
 */@Component("LastDateBelow")
public class LastDateBelowValue extends DateValueProcessor{
    /**
     * the first value date
     */
    private Optional<LocalDateTime> date=Optional.empty();
    /**
     * the min value
     */
    private double lastValue;
    public LastDateBelowValue() {
    }
    @Override
    public void before() {
        if(inputParameters.isEmpty()){
            throw new IllegalArgumentException("Missing parameter");
        }
        this.lastValue = Double.parseDouble(inputParameters.get(0));
        // set mode to above\
        this.processorName="Last Date With Value Below "+ inputParameters.get(0);
        clearProcessedTextValues();
        yearlyDataValues.clear();

    }

    @Override
    public void onEndDate(String date) {
            String text = "Last instance of " + dataType + " Below " + lastValue;
            addValue(date,this.date, text);
            this.date = Optional.empty();
    }
    @Override
    public void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value<= lastValue) {
            this.date= Optional.of(LocalDateTime.parse(date));
        }
    }
}
